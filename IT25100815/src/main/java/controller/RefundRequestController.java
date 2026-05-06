package com.tourflex.controller;

import com.tourflex.model.Booking;
import com.tourflex.model.RefundRequest;
import com.tourflex.service.BookingService;
import com.tourflex.service.RefundRequestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/refund")
public class RefundRequestController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RefundRequestService refundRequestService;

    @GetMapping("/request/{bookingId}")
    public String showRefundForm(@PathVariable int bookingId, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        Booking booking = bookingService.getBookingById(bookingId);
        model.addAttribute("booking", booking);
        return "refund-request";
    }

    @PostMapping("/save")
    public String saveRefundRequest(@RequestParam int bookingId,
                                    @RequestParam String customerName,
                                    @RequestParam String customerEmail,
                                    @RequestParam String packageName,
                                    @RequestParam String bankName,
                                    @RequestParam String accountHolderName,
                                    @RequestParam String accountNumber,
                                    @RequestParam String branchName,
                                    @RequestParam String note,
                                    Model model,
                                    HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/user/login-page";
        }

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setBookingId(bookingId);
        refundRequest.setCustomerName(customerName);
        refundRequest.setCustomerEmail(customerEmail);
        refundRequest.setPackageName(packageName);
        refundRequest.setBankName(bankName);
        refundRequest.setAccountHolderName(accountHolderName);
        refundRequest.setAccountNumber(accountNumber);
        refundRequest.setBranchName(branchName);
        refundRequest.setNote(note);

        refundRequestService.saveRefundRequest(refundRequest);

        model.addAttribute("message", "Refund request submitted successfully!");
        return "refund-request-success";
    }

    @GetMapping("/list")
    public String showRefundList(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("refunds", refundRequestService.getAllRefundRequests());
        return "refund-list";
    }

    @GetMapping("/complete/{id}")
    public String markRefundCompleted(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        refundRequestService.markRefundAsCompleted(id);
        return "redirect:/refund/list";
    }
}