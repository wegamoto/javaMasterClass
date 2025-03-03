import React, { useEffect, useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/api/ads/active"; // ใช้ API Spring Boot

const Home = () => {
  const [ads, setAds] = useState([]);

  useEffect(() => {
    axios
      .get(API_URL)
      .then((response) => {
        setAds(response.data);
      })
      .catch((error) => {
        console.error("Error fetching ads:", error);
      });
  }, []);

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-bold mb-4">โฆษณาแนะนำ</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {ads.map((ad) => (
          <div key={ad.id} className="border rounded-lg p-4 shadow-lg">
            <img src={ad.imageUrl} alt={ad.title} className="w-full h-40 object-cover rounded-lg" />
            <h2 className="text-lg font-semibold mt-2">{ad.title}</h2>
            <p className="text-sm text-gray-600">{ad.description}</p>
            <a href={ad.targetUrl} target="_blank" className="text-blue-500 mt-2 block">
              ดูรายละเอียด
            </a>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Home;
