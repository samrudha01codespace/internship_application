package com.samrudha.bidai.ui.data

import com.samrudha.bidai.R
import com.samrudha.bidai.ui.models.BannerUiModel
import com.samrudha.bidai.ui.models.CategoryUiModel
import com.samrudha.bidai.ui.models.FeatureUiModel
import com.samrudha.bidai.ui.models.ProductUiModel
import com.samrudha.bidai.ui.models.TestimonialUiModel

/**
 * Static mock content shaped for easy swap to GET /api/products later.
 * Keep field names aligned with backend response mapping.
 */
object SampleHomeData {

    val carFilters = listOf(
        "Sports & Classics",
        "Vintage Cars",
        "Electric Cars",
        "Garage",
        "Hybrid",
        "Electric",
        "SUVs",
        "Sedans"
    )

    val bikeFilters = listOf(
        "Sports Bikes",
        "Cruisers",
        "Off-Road",
        "Scooters",
        "Electric",
        "Standard",
        "Touring"
    )

    val categories = listOf(
        CategoryUiModel("cars", "Cars", "🚗", R.drawable.cat_cars, 0xFF0668E1),
        CategoryUiModel("real_estate", "Real Estate", "🏠", R.drawable.cat_real_estate, 0xFF00B894),
        CategoryUiModel("mobiles", "Mobiles", "📱", R.drawable.cat_mobiles, 0xFF6C5CE7),
        CategoryUiModel("jobs", "Jobs", "💼", R.drawable.cat_jobs, 0xFFE17055),
        CategoryUiModel("bikes", "Bikes", "🏍️", R.drawable.cat_bikes, 0xFF0984E3),
        CategoryUiModel("electronics", "Electronics", "🖥️", R.drawable.cat_electronics, 0xFFD63031),
        CategoryUiModel("home_garden", "Home & Garden", "🛋️", R.drawable.cat_home_garden, 0xFF2D3436),
        CategoryUiModel("beauty", "Beauty", "👠", R.drawable.cat_beauty, 0xFFE84393),
        CategoryUiModel("clothing", "Clothing", "👕", R.drawable.cat_clothing, 0xFFFD79A8),
        CategoryUiModel("books", "Books", "📚", R.drawable.cat_books, 0xFFA29BFE),
        CategoryUiModel("arts", "Arts and Crafts", "🎨", R.drawable.cat_arts, 0xFF55A3FF),
        CategoryUiModel("services", "Services", "🔧", R.drawable.cat_services, 0xFF00CEC9),
        CategoryUiModel("yodha", "Yodha", "🪖", R.drawable.cat_yodha, 0xFF636E72),
        CategoryUiModel("agriculture", "Agriculture", "🚜", R.drawable.cat_agriculture, 0xFF27AE60),
        CategoryUiModel("general", "General Service", "⚙️", R.drawable.cat_general, 0xFFFDCB6E)
    )

    val heroBanners = listOf(
        BannerUiModel(
            id = "hero_1",
            imageRes = R.drawable.banner_hero,
            title = "Find Your\nDream job\nwith\nBID.AI"
        )
    )

    val promoBanners = listOf(
        BannerUiModel(
            id = "promo_furniture",
            imageRes = R.drawable.banner_promo,
            title = null,
            cta = "Start Sell Now"
        )
    )

    val cars = listOf(
        ProductUiModel(
            id = "car_1",
            title = "Black Tata punch",
            subtitle = "Tata brand",
            price = "₹ 2,15,000",
            originalPrice = "₹ 2,95,000",
            ecoScore = "82/100",
            isVerified = true,
            location = "Bengaluru",
            date = "28 July",
            imageRes = R.drawable.product_car_1
        ),
        ProductUiModel(
            id = "car_2",
            title = "White Hyundai i20",
            subtitle = "Hyundai brand",
            price = "₹ 4,50,000",
            originalPrice = "₹ 5,20,000",
            ecoScore = "76/100",
            isVerified = true,
            location = "Pune",
            date = "26 July",
            imageRes = R.drawable.product_car_2
        ),
        ProductUiModel(
            id = "car_3",
            title = "Red Maruti Swift",
            subtitle = "Maruti brand",
            price = "₹ 3,10,000",
            ecoScore = "71/100",
            isVerified = false,
            location = "Mumbai",
            date = "25 July",
            imageRes = R.drawable.product_car_1
        ),
        ProductUiModel(
            id = "car_4",
            title = "Blue Honda City",
            subtitle = "Honda brand",
            price = "₹ 6,75,000",
            originalPrice = "₹ 7,40,000",
            isVerified = true,
            location = "Delhi",
            date = "24 July",
            imageRes = R.drawable.product_car_2
        )
    )

    val bikes = listOf(
        ProductUiModel(
            id = "bike_1",
            title = "MT 16",
            subtitle = "Yamaha brand",
            price = "₹ 1,45,000",
            originalPrice = "₹ 1,65,000",
            ecoScore = "88/100",
            isVerified = true,
            location = "Bengaluru",
            date = "27 July",
            imageRes = R.drawable.product_bike_1
        ),
        ProductUiModel(
            id = "bike_2",
            title = "Himalayan 450",
            subtitle = "Royal Enfield",
            price = "₹ 2,85,000",
            ecoScore = "84/100",
            isVerified = true,
            location = "Chennai",
            date = "26 July",
            imageRes = R.drawable.product_bike_2
        ),
        ProductUiModel(
            id = "bike_3",
            title = "KTM Duke 200",
            subtitle = "KTM brand",
            price = "₹ 2,10,000",
            originalPrice = "₹ 2,35,000",
            isVerified = false,
            location = "Hyderabad",
            date = "25 July",
            imageRes = R.drawable.product_bike_1
        )
    )

    val recommendations = listOf(
        ProductUiModel(
            id = "rec_home",
            title = "Open house",
            subtitle = "2BHK Villa",
            price = "₹ 45,00,000",
            isVerified = true,
            location = "Bengaluru",
            date = "28 July",
            imageRes = R.drawable.product_home
        ),
        ProductUiModel(
            id = "rec_phone",
            title = "Vivo V20",
            subtitle = "Vivo brand",
            price = "₹ 18,500",
            originalPrice = "₹ 24,990",
            ecoScore = "90/100",
            isVerified = true,
            location = "Pune",
            date = "27 July",
            imageRes = R.drawable.product_phone
        ),
        ProductUiModel(
            id = "rec_jeans",
            title = "Black Slim fit jeans",
            subtitle = "Levi's brand",
            price = "₹ 1,999",
            originalPrice = "₹ 3,499",
            isVerified = false,
            location = "Mumbai",
            date = "26 July",
            imageRes = R.drawable.product_jeans
        ),
        ProductUiModel(
            id = "rec_bike",
            title = "MT 16",
            subtitle = "Yamaha brand",
            price = "₹ 1,45,000",
            ecoScore = "88/100",
            isVerified = true,
            location = "Bengaluru",
            date = "25 July",
            imageRes = R.drawable.product_bike_1
        ),
        ProductUiModel(
            id = "rec_elec",
            title = "Smart LED 55\"",
            subtitle = "Samsung brand",
            price = "₹ 32,999",
            originalPrice = "₹ 41,900",
            isVerified = true,
            location = "Delhi",
            date = "24 July",
            imageRes = R.drawable.product_elec_1
        ),
        ProductUiModel(
            id = "rec_car",
            title = "Black Tata punch",
            subtitle = "Tata brand",
            price = "₹ 2,15,000",
            originalPrice = "₹ 2,95,000",
            isVerified = true,
            location = "Bengaluru",
            date = "23 July",
            imageRes = R.drawable.product_car_1
        )
    )

    val features = listOf(
        FeatureUiModel(
            id = "f1",
            body = "More then 13+ Categories are in Bid.ai",
            imageRes = R.drawable.ic_feature_categories
        ),
        FeatureUiModel(
            id = "f2",
            body = "Sell your unused items in 30 seconds With One click sell",
            imageRes = R.drawable.ic_feature_oneclick
        ),
        FeatureUiModel(
            id = "f3",
            body = "Earn upto ₹100000000 in BID.ai credits by referral!",
            imageRes = R.drawable.ic_feature_referral
        )
    )

    val testimonials = listOf(
        TestimonialUiModel(
            id = "t1",
            quote = "I sold my cooler, sofa, and gym set using BID.ai. Now even my apartment group uses it to sell or swap items. It's that useful!",
            name = "Neha A.",
            city = "Mumbai"
        ),
        TestimonialUiModel(
            id = "t2",
            quote = "I listed my bike on BID.ai just to try it out, and boom – got an offer the same day! No commission, no calls from dealers. Totally smooth.",
            name = "Ravi S.",
            city = "Pune"
        ),
        TestimonialUiModel(
            id = "t3",
            quote = "As a working mom, I don't have time to haggle. BID.ai took care of it all – price, visibility, and even reminders",
            name = "Anjali",
            city = "Delhi"
        )
    )
}
