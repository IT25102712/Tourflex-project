package com.tourflex.controller;

import com.tourflex.model.Hotel;
import com.tourflex.service.HotelService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/add-page")
    public String showAddHotelPage(HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        return "add-hotel";
    }

    @PostMapping("/save")
    public String saveHotel(@RequestParam String hotelName,
                            @RequestParam String location,
                            @RequestParam String starRating,
                            HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        Hotel hotel = new Hotel();
        hotel.setHotelName(hotelName);
        hotel.setLocation(location);
        hotel.setStarRating(starRating);

        hotelService.saveHotel(hotel);
        return "redirect:/hotel/manage";
    }

    @GetMapping("/manage")
    public String manageHotels(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("hotels", hotelService.getAllHotels());
        return "manage-hotels";
    }

    @GetMapping("/delete/{id}")
    public String deleteHotel(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        hotelService.deleteHotel(id);
        return "redirect:/hotel/manage";
    }

    @ResponseBody
    @GetMapping("/by-location")
    public List<Hotel> getHotelsByLocation(@RequestParam String location) {
        return hotelService.getHotelsByLocation(location);
    }
}