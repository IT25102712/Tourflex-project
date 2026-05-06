package com.tourflex.service;

import com.tourflex.model.Booking;
import com.tourflex.model.RefundRequest;
import com.tourflex.repository.BookingRepository;
import com.tourflex.repository.RefundRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundRequestService {

    @Autowired
    private RefundRequestRepository refundRequestRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public RefundRequest saveRefundRequest(RefundRequest refundRequest) {
        refundRequest.setRefundStatus("Pending");
        return refundRequestRepository.save(refundRequest);
    }

    public List<RefundRequest> getAllRefundRequests() {
        return refundRequestRepository.findAll();
    }

    public RefundRequest getRefundRequestById(int id) {
        return refundRequestRepository.findById(id).orElse(null);
    }

    public void markRefundAsCompleted(int refundRequestId) {
        RefundRequest refundRequest = refundRequestRepository.findById(refundRequestId).orElse(null);

        if (refundRequest != null) {
            refundRequest.setRefundStatus("Completed");
            refundRequestRepository.save(refundRequest);

            Booking booking = bookingRepository.findById(refundRequest.getBookingId()).orElse(null);
            if (booking != null) {
                booking.setRefundStatus("Completed");
                bookingRepository.save(booking);
            }
        }
    }
}