package com.tourflex.config;

import com.tourflex.model.Hotel;
import com.tourflex.repository.HotelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final HotelRepository hotelRepository;

    public DataLoader(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public void run(String... args) {
        if (hotelRepository.count() == 0) {

            saveHotel("98 Acres Resort", "Ella", "5 Star");
            saveHotel("Morning Dew Boutique", "Ella", "4 Star");
            saveHotel("Ella Mount Heaven", "Ella", "3 Star");

            saveHotel("The Grand Kandyan", "Kandy", "5 Star");
            saveHotel("Earl's Regent", "Kandy", "4 Star");
            saveHotel("Hotel Topaz", "Kandy", "3 Star");

            saveHotel("The Grand Hotel", "Nuwara Eliya", "5 Star");
            saveHotel("Araliya Green Hills", "Nuwara Eliya", "4 Star");
            saveHotel("Galway Heights", "Nuwara Eliya", "3 Star");

            saveHotel("Jetwing Yala", "Yala", "5 Star");
            saveHotel("Cinnamon Wild Yala", "Yala", "4 Star");
            saveHotel("Leopard Nest", "Yala", "3 Star");

            saveHotel("Taj Bentota", "Bentota", "5 Star");
            saveHotel("Heritance Ahungalla", "Bentota", "4 Star");
            saveHotel("Bentota Village", "Bentota", "3 Star");

            saveHotel("Amari Galle", "Galle", "5 Star");
            saveHotel("Radisson Blu Galle", "Galle", "4 Star");
            saveHotel("Lady Hill Hotel", "Galle", "3 Star");

            saveHotel("Aliya Resort", "Sigiriya", "5 Star");
            saveHotel("Hotel Sigiriya", "Sigiriya", "4 Star");
            saveHotel("Sigiriya Village", "Sigiriya", "3 Star");

            saveHotel("Amaranthe Bay", "Trincomalee", "5 Star");
            saveHotel("Trinco Blu by Cinnamon", "Trincomalee", "4 Star");
            saveHotel("Anantamaa Hotel", "Trincomalee", "3 Star");

            saveHotel("Weligama Bay Marriott", "Mirissa", "5 Star");
            saveHotel("Mandara Resort Mirissa", "Mirissa", "4 Star");
            saveHotel("Paradise Beach Club", "Mirissa", "3 Star");

            saveHotel("Shangri-La Colombo", "Colombo", "5 Star");
            saveHotel("Cinnamon Grand Colombo", "Colombo", "5 Star");
            saveHotel("Fairway Colombo", "Colombo", "4 Star");

            saveHotel("Jetwing Jaffna", "Jaffna", "4 Star");
            saveHotel("North Gate Hotel", "Jaffna", "3 Star");

            saveHotel("Uga Ulagalla", "Anuradhapura", "5 Star");
            saveHotel("Rajarata Hotel", "Anuradhapura", "4 Star");
            saveHotel("Milano Tourist Rest", "Anuradhapura", "3 Star");

            saveHotel("Hotel Sudu Araliya", "Polonnaruwa", "4 Star");
            saveHotel("EKHO Lake House", "Polonnaruwa", "3 Star");

            saveHotel("Amaya Lake", "Dambulla", "5 Star");
            saveHotel("Jetwing Lake", "Dambulla", "5 Star");
            saveHotel("Pelwehera Village Resort", "Dambulla", "3 Star");

            saveHotel("Cinnamon Lodge", "Habarana", "5 Star");
            saveHotel("Habarana Village", "Habarana", "4 Star");
            saveHotel("Acme Grand Hotel", "Habarana", "3 Star");

            saveHotel("Anantaya Resort", "Pasikuda", "5 Star");
            saveHotel("Amaya Beach", "Pasikuda", "4 Star");
            saveHotel("Sunrise Pasikuda", "Pasikuda", "3 Star");

            saveHotel("Jetwing Surf", "Arugam Bay", "4 Star");
            saveHotel("The Bay", "Arugam Bay", "3 Star");

            saveHotel("Hikka Tranz by Cinnamon", "Hikkaduwa", "4 Star");
            saveHotel("Coral Sands Hotel", "Hikkaduwa", "3 Star");

            saveHotel("Thaproban Pavilion", "Unawatuna", "5 Star");
            saveHotel("Cocobay Unawatuna", "Unawatuna", "4 Star");
            saveHotel("Rockside Cabanas", "Unawatuna", "3 Star");

            saveHotel("Cape Weligama", "Weligama", "5 Star");
            saveHotel("W15 Weligama", "Weligama", "4 Star");
            saveHotel("Ekho Weligama", "Weligama", "3 Star");

            saveHotel("Anantara Peace Haven", "Tangalle", "5 Star");
            saveHotel("Good Karma", "Tangalle", "4 Star");
            saveHotel("Eva Lanka", "Tangalle", "3 Star");

            saveHotel("Jetwing Blue", "Negombo", "5 Star");
            saveHotel("Heritance Negombo", "Negombo", "5 Star");
            saveHotel("Camelot Beach Hotel", "Negombo", "4 Star");

            saveHotel("Dolphin Beach Resort", "Kalpitiya", "4 Star");
            saveHotel("Blue Whale Resort", "Kalpitiya", "3 Star");

            saveHotel("Palmstone Retreat", "Kitulgala", "4 Star");
            saveHotel("Rafters Retreat", "Kitulgala", "3 Star");

            saveHotel("Laja Resort", "Belihuloya", "4 Star");
            saveHotel("Belihuloya Rest House", "Belihuloya", "3 Star");

            saveHotel("The Hill Club", "Horton Plains", "4 Star");
            saveHotel("Galway Forest Lodge", "Horton Plains", "3 Star");

            saveHotel("Bandarawela Hotel", "Bandarawela", "4 Star");
            saveHotel("Orient Hotel", "Bandarawela", "3 Star");

            saveHotel("Grand Star Hotel", "Badulla", "3 Star");
            saveHotel("360 City View", "Badulla", "3 Star");

            saveHotel("Terrace Cinnamon View", "Ratnapura", "4 Star");
            saveHotel("Centauria Hill Resort", "Ratnapura", "3 Star");

            saveHotel("Rainforest Eco Lodge", "Sinharaja", "4 Star");
            saveHotel("Blue Magpie Lodge", "Sinharaja", "3 Star");

            saveHotel("Grand Udawalawe Safari Resort", "Udawalawe", "4 Star");
            saveHotel("Kalu's Hideaway", "Udawalawe", "3 Star");

            saveHotel("Deer Park Hotel", "Minneriya", "4 Star");
            saveHotel("Elephant Corridor", "Minneriya", "3 Star");

            saveHotel("Big Game Camp", "Wilpattu", "4 Star");
            saveHotel("Leopard Trails", "Wilpattu", "5 Star");

            saveHotel("Hotel Elephant Park", "Pinnawala", "4 Star");
            saveHotel("Pinnalanda Hotel", "Pinnawala", "3 Star");

            saveHotel("Culture Resort", "Matara", "4 Star");
            saveHotel("Beach Inns", "Matara", "3 Star");

            saveHotel("Hotel East Lagoon", "Batticaloa", "3 Star");
            saveHotel("Amaya Beach Pasikudah", "Batticaloa", "4 Star");

            saveHotel("Gal Oya Lodge", "Ampara", "4 Star");
            saveHotel("Forest Rock Garden", "Ampara", "3 Star");

            saveHotel("The Kandyan Reach", "Kurunegala", "4 Star");
            saveHotel("Hotel Blue Sky", "Kurunegala", "3 Star");

            saveHotel("The Grand Mountain", "Matale", "4 Star");
            saveHotel("Riverston Holiday Home", "Matale", "3 Star");

            saveHotel("Mapakada Village", "Mahiyanganaya", "4 Star");
            saveHotel("Kevan's Casa", "Mahiyanganaya", "3 Star");

            saveHotel("Hotel Agape", "Mannar", "3 Star");
            saveHotel("Four Teess Rest Inn", "Mannar", "3 Star");

            saveHotel("Hotel North Way", "Vavuniya", "3 Star");
            saveHotel("Nelly Star Hotel", "Vavuniya", "3 Star");

            saveHotel("Ahi House", "Kilinochchi", "3 Star");
            saveHotel("Friends Paradise", "Kilinochchi", "3 Star");

            saveHotel("The Calm Resort", "Mullaitivu", "3 Star");
            saveHotel("Sea Breeze Guest", "Mullaitivu", "3 Star");

            saveHotel("Anantaya Resort Chilaw", "Chilaw", "5 Star");
            saveHotel("Carolina Beach", "Chilaw", "4 Star");

            saveHotel("Ruwala Resort", "Puttalam", "4 Star");
            saveHotel("Ocean View Resort", "Puttalam", "3 Star");

            saveHotel("Cinnamon Bey", "Beruwala", "5 Star");
            saveHotel("Eden Resort", "Beruwala", "4 Star");

            saveHotel("Club Bentota", "Aluthgama", "4 Star");
            saveHotel("Lanka Princess", "Aluthgama", "4 Star");

            saveHotel("The Fortress", "Ahangama", "5 Star");
            saveHotel("Insight Resort", "Ahangama", "4 Star");

            saveHotel("Chaarya Resort", "Tissamaharama", "4 Star");
            saveHotel("Kithala Resort", "Tissamaharama", "3 Star");
        }
    }

    private void saveHotel(String hotelName, String location, String starRating) {
        Hotel hotel = new Hotel();
        hotel.setHotelName(hotelName);
        hotel.setLocation(location);
        hotel.setStarRating(starRating);
        hotelRepository.save(hotel);
    }
}