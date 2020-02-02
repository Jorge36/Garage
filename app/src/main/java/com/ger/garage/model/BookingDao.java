package com.ger.garage.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ger.garage.Presenter.FirebaseException;
import com.ger.garage.Presenter.FirebaseListener;
import com.ger.garage.Presenter.FirebaseListener2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*


    Class BookingDao: Represent a class which access to objects/fields/documents/collections in the database and
    get/create/update information

 */

public class BookingDao {


    private FirebaseFirestore db; // database
    private FirebaseAuth mFirebaseAuth; // autentication
    // path to databases
    private final String bookingByDateCollectionPath = "garage/bookingInformation/bookingByDate";
    private final String bookingsCollectionPath = "garage/bookingInformation/bookings";
    private final String bookingByUserCollectionPath = "garage/userInformation/users";
    private final String counterDocument = "counter";
    private final String internalError = "Internal Error";
    private final String errorGettingDocumentMessage = "Error getting documents: ";
    private final String errorMsg1 = "The shift ";
    private final String errorMsg2 = " is not available anymore. Please try with another date and/or shift";
    private final String errorUpdateStatus = "The update was not performed. Try it again";
    private final String errorUpdateMechanics = "The update was not performed. Try it again";
    // Listeners which listen if there are changes in the database
    private ListenerRegistration listenerBookingsByUser;
    private ListenerRegistration listenerBookingsByDate;
    private ListenerRegistration listenerBookingsByRangeOfDate;


    public BookingDao() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


    }


    // get quantity of bookings by shift
    public void getQuantityOfBookingByShift(final LocalDate date, final ArrayList<Shift> shifts, final FirebaseListener listener) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY"); // format to get the name of the document to retrieve
        final String dateAux = formatter.format(date);

        // document docRef -  get data from this link
        DocumentReference docRef = db.collection(bookingByDateCollectionPath).document(dateAux);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) { // successful
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        // create a map which has the pair: Number of shift - Quantity
                        Map<Integer, Integer> resultData = new HashMap<>();

                        for (Shift shift: shifts) {

                            if (document.contains(shift.getId().toString()))
                                resultData.put(shift.getId(), Integer.parseInt(String.valueOf((Long) document.get(shift.getId().toString()))));
                        }

                        listener.onSuccess(resultData); // return new structure

                    } else { // this document is not created in the database - create the document
                            //  and return the structure with quantities equal to 0

                        createShiftStructure(dateAux, shifts, listener);

                    }
                } else {
                }
            }

            // create an empty document in garage/bookingInformation/bookingByDate
            private void createShiftStructure(String date, final ArrayList<Shift> shifts, final FirebaseListener listener) {

                final Map<String, Object> docData = new HashMap<>();
                for (Shift shift: shifts) {

                    docData.put(shift.getId().toString(),0);

                }
                // garage/bookingInformation/bookingByDate
                db.collection(bookingByDateCollectionPath).document(date)
                        .set(docData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // create structure with quantity 0
                                Map<Integer, Integer> resultData = new HashMap<>();

                                for (Shift shift: shifts) {

                                    resultData.put(shift.getId(), 0);
                                }

                                listener.onSuccess(resultData);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() { // error, it can't create the structure
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.onFailure(new FirebaseException(internalError));
                            }
                        });


            }
        });

    }

    // create booking with all the data
    public void createBooking(final Booking booking, final Integer quantityTotalBookingByShift, final FirebaseListener listener) {

        // doc ref to get counter for new id
        final DocumentReference counterDocRef = db.collection(bookingsCollectionPath).document(counterDocument);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");

        final String dateAux = formatter.format(booking.getDate());

        // check again if there are space for the booking
        final DocumentReference dateRef = db.collection(bookingByDateCollectionPath).document(dateAux);

        // run atomic operation called transaction
        db.runTransaction(new Transaction.Function<Integer>() {


            @Nullable
            @Override
            public Integer apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                final String idBookingCounter = "idBooking";
                final String listOfBookings = "ListOfBookings";

                // Read
                DocumentSnapshot counterSnapshot = transaction.get(counterDocRef);

                // get counter last id
                Integer counter = Integer.parseInt(String.valueOf(counterSnapshot.getLong(idBookingCounter)));

                counter++; // increment counter

                // set new counter
                booking.setId(counter);

                DocumentSnapshot dateSnapshot = transaction.get(dateRef);

                Integer shiftQuantity;

                // check every shift that belongs to the booking has space
                for (Shift shift: booking.getShifts()) {

                    shiftQuantity = Integer.parseInt(String.valueOf(dateSnapshot.getLong(shift.getId().toString())));

                    // current quantity booking is less than quantityTotalBookingByShift
                    if (shiftQuantity == quantityTotalBookingByShift) { // abort retry operation

                        throw new FirebaseFirestoreException(errorMsg1 + shift.toString() + errorMsg2,
                        FirebaseFirestoreException.Code.ABORTED);

                    }

                }
                // Write counter incremented
                transaction.update(counterDocRef, idBookingCounter, counter);

                //update quantity of the shift plus 1 for that day garage/bookingInformation/bookingByDate
                for (Shift shift: booking.getShifts()) {

                    shiftQuantity = Integer.parseInt(String.valueOf(dateSnapshot.getLong(shift.getId().toString()))) + 1;

                    transaction.update(dateRef, shift.getId().toString(), shiftQuantity);

                }

                // write information about bookings in the links where bookings are
                DocumentReference bookingsDocRef = db.collection(bookingsCollectionPath).document(booking.getId().toString());


                Map<String, Object> bookingToSave = createDataBookingToSave(booking);

                transaction.set(bookingsDocRef, bookingToSave);

                // write booking inside of the user subtree
                transaction.set(db.collection(bookingByUserCollectionPath).document(booking.getUser().getId()).collection(listOfBookings).document(booking.getId().toString()), bookingToSave);

                return counter;

            }

            private Map<String, Object> createDataBookingToSave(Booking booking) {

                // create structure of the booking
                Map<String, Object> docDataBooking = new HashMap<>();

                docDataBooking.put("id", booking.getId());
                docDataBooking.put("date", new Timestamp(java.sql.Date.valueOf(booking.getDate().toString())));
                docDataBooking.put("createdAt", FieldValue.serverTimestamp());
                docDataBooking.put("type", booking.getType());
                docDataBooking.put("comments", booking.getComments());
                docDataBooking.put("vehicle", booking.getVehicle());
                docDataBooking.put("status", booking.getStatus());
                docDataBooking.put("shifts", booking.getShifts());
                docDataBooking.put("user", booking.getUser());

                return docDataBooking;

            }
        }).addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer aLong) {
                    listener.onSuccess(aLong);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFailure(new FirebaseException(e.getMessage()));
            }
        });

    }

    // get bookings from the subtree user
    public void getBookingsByUser(String userId, final FirebaseListener listener) {

        final String listOfBookings = "ListOfBookings";

        CollectionReference colRef = db.collection(bookingByUserCollectionPath).document(userId).collection(listOfBookings);

        Source source = Source.SERVER; // it bring information from server, jump caches

        colRef.get(source).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    ArrayList<Booking> bookings = new ArrayList<>();

                    for (QueryDocumentSnapshot document: task.getResult()) {

                        bookings.add(buildBooking(document)); // build a object booking

                    }

                    listener.onSuccess(bookings);


                } else {

                    listener.onFailure(new FirebaseException(errorGettingDocumentMessage + task.getException().getMessage()));

                }


            }
        });

        // listener which list if there is a change in the subtree user
        listenerBookingsByUser = colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }

                ArrayList<Booking> bookings = new ArrayList<>();

                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {


                    bookings.add(buildBooking(document));

                }

                listener.onSuccess(bookings);

            }
        });

    }

    // remove listener
    public void removeListenerBookingsByRef() {


        if (listenerBookingsByUser != null)
            listenerBookingsByUser.remove();
    }


    // build structure of a booking, input document which is got from database
    private Booking buildBooking(QueryDocumentSnapshot document) {

        Integer id = Integer.parseInt(document.getId());
        LocalDate date = document.getDate("date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Timestamp createdAt = document.getTimestamp("createdAt");
        String type = (String) document.get("type");
        String comments = (String) document.get("comments");

        // vehicle
        Map<String, Object> vehicleMap = (Map<String, Object>) document.get("vehicle");
        // make of the vehicle
        Map<String, Object> makeMap = (Map<String, Object>) vehicleMap.get("make");
        Make make = new Make((String) makeMap.get("name"), (String) makeMap.get("model"));
        Vehicle vehicle = new Vehicle(make, (String) vehicleMap.get("numberPlate"), (String) vehicleMap.get("engineType"), (String) vehicleMap.get("type"));

        //Create Mechanic
        //contains(String field)
        String status = (String) document.get("status");
        // Create cost if this exist

        Map<String, Object> userMap = (Map<String, Object>) document.get("user");

        User user = new User((String) userMap.get("id"), (String) userMap.get("name"), (String) userMap.get("mobilePhoneNumber"), (String) userMap.get("email"), "", UserType.user);


        ArrayList<Map<String,Object>> shiftsOfHashMap = (ArrayList<Map<String,Object>>) document.get("shifts");

        ArrayList<Shift> shifts = new ArrayList<>();

        Integer idShift;
        String description;
        LocalTime timeStart;
        LocalTime timeEnd;

        Map<String, Object> timeHashMap;
        // build shift of the booking
        for (Map<String, Object> s: shiftsOfHashMap) {

            idShift = Integer.parseInt(s.get("id").toString());
            description = (String) s.get("description");

            timeHashMap = (Map<String, Object>) s.get("timeStart");

            timeStart = LocalTime.of(Integer.parseInt(timeHashMap.get("hour").toString()), Integer.parseInt(timeHashMap.get("minute").toString()), Integer.parseInt(timeHashMap.get("second").toString()), Integer.parseInt(timeHashMap.get("nano").toString()));

            timeHashMap = (Map<String, Object>) s.get("timeEnd");

            timeEnd = LocalTime.of(Integer.parseInt(timeHashMap.get("hour").toString()), Integer.parseInt(timeHashMap.get("minute").toString()), Integer.parseInt(timeHashMap.get("second").toString()), Integer.parseInt(timeHashMap.get("nano").toString()));

            shifts.add(new Shift(idShift, description, timeStart, timeEnd));

        }

        // Mechanic map
        Map<String, Object> mechanicMap = (Map<String, Object>) document.get("mechanic");
        Mechanic  mechanic;
        if (mechanicMap != null)
            mechanic =  new Mechanic(Integer.parseInt(mechanicMap.get("id").toString()), (String) mechanicMap.get("name"));
        else
            mechanic = null;
        // Mechanic map

        return new Booking(id, date, createdAt, type, comments, vehicle, mechanic, status, null, user, shifts);
    }


    // get bookings by date
    public void getBookingsByDate(LocalDate fDate, LocalDate sDate, FirebaseListener2 listener2) {

        if (sDate == null)
            executeQueryByADate(fDate, listener2); // a date
        else
            executeQueryByARangeOfDates(fDate, sDate, listener2); // range of dates


    }

    // create a listener to get bookings, filter by range of dates
    private void executeQueryByARangeOfDates(LocalDate fDate, LocalDate sDate, final FirebaseListener2 listener2) {


        final CollectionReference colRef = db.collection(bookingsCollectionPath);

        Timestamp fTimestamp = new Timestamp(java.sql.Date.valueOf(fDate.toString()));
        Timestamp sTimestamp = new Timestamp(java.sql.Date.valueOf(sDate.toString()));

        listenerBookingsByRangeOfDate = colRef.whereGreaterThanOrEqualTo("date", fTimestamp).whereLessThanOrEqualTo("date", sTimestamp).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {

                    listener2.onFailure(new FirebaseException(errorGettingDocumentMessage + e.getMessage()));

                }

                ArrayList<Booking> bookings = new ArrayList<>();

                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {

                    bookings.add(buildBooking(document));

                }

                listener2.onSuccess(bookings);

            }
        });

    }

    public void removeListenerBookings() {


        if (listenerBookingsByDate != null)
            listenerBookingsByDate.remove();

        if (listenerBookingsByRangeOfDate != null)
            listenerBookingsByRangeOfDate.remove();

    }

    // create a listener to get bookings, filter by a date
    private void executeQueryByADate(LocalDate date, final FirebaseListener2 listener2) {

        final CollectionReference colRef = db.collection(bookingsCollectionPath);

        Timestamp timestamp = new Timestamp(java.sql.Date.valueOf(date.toString()));

        listenerBookingsByDate = colRef.whereEqualTo("date", timestamp).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {

                    listener2.onFailure(new FirebaseException(errorGettingDocumentMessage + e.getMessage()));

                }

                ArrayList<Booking> bookings = new ArrayList<>();

                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {

                    bookings.add(buildBooking(document));

                }

                listener2.onSuccess(bookings);

            }
        });

    }

    // change status of a booking
    public void changeStatus(Booking booking, final String newStatus, final FirebaseListener2 listener) {

        final String listOfBookings = "ListOfBookings";
        final String statusField = "status";

        // batch atomic operation
        WriteBatch batch = db.batch();

        // change status in the booking subtree
        DocumentReference bookingRef = db.collection(bookingsCollectionPath).document(booking.getId().toString());
        batch.update(bookingRef, statusField, newStatus);
        // change status in the user subtree
        DocumentReference userRef = db.collection(bookingByUserCollectionPath).document(booking.getUser().getId()).collection(listOfBookings).document(booking.getId().toString());
        batch.update(userRef, statusField, newStatus);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())

                    listener.onSuccess(newStatus);

                else

                    listener.onFailure( new FirebaseException(errorUpdateStatus + task.getException().getMessage()));

            }
        });

    }

    // set mechanic to a booking in user subtree and booking subtree
    public void setMechanicToAbooking(HashMap<Booking, Mechanic> mechanicToBooking, final FirebaseListener2 listener) {

        final String listOfBookings = "ListOfBookings";
        final String mechanicField = "mechanic";

        WriteBatch batch = db.batch();

        final ArrayList<Booking> bookings = new ArrayList<>();
        Map<String, Object> dataMechanic = new HashMap<>();


        Iterator it = mechanicToBooking.entrySet().iterator();
        Map.Entry pair;
        DocumentReference bookingRef;
        DocumentReference userRef;
        Booking booking = null;
        Mechanic mechanic = null;

        // loop to set every mechanic with the booking associated
        while (it.hasNext()) {

            // get the pair booking mechanic
            pair = ((Map.Entry) it.next());

            // get Booking
            booking = (Booking) pair.getKey();
            mechanic = (Mechanic) pair.getValue();

            booking.setMechanic(mechanic);

            bookings.add(booking);

            dataMechanic.put("mechanic", mechanic);

            // Booking ref
            bookingRef = db.collection(bookingsCollectionPath).document(booking.getId().toString());
            batch.set(bookingRef, dataMechanic, SetOptions.merge());

            userRef = db.collection(bookingByUserCollectionPath).document(booking.getUser().getId()).collection(listOfBookings).document(booking.getId().toString());
            batch.set(userRef, dataMechanic, SetOptions.merge());

            dataMechanic.clear();

        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())

                    listener.onSuccessUpdateMechanic(bookings);

                else

                    listener.onFailure( new FirebaseException(errorUpdateMechanics + task.getException().getMessage()));

            }
        });

    }


}

