"use client";

import { useState } from "react";
import AirportQuery from "./AirportQuery";
import RunwayTypes from "./RunwayQuery";
import LatitudeReportComponent from "./ReportLatitude";
import Countries from "./Countries";

export default function HomeComponent() {
  const [activeTab, setActiveTab] = useState<"airportQuery" | "runwayTypes" | "latitude" | "topCountries">("airportQuery");

  return (
    <div className="min-h-screen p-4 bg-gray-100">
      <h1 className="text-3xl font-bold mb-4 text-center">Worldwide Dashboard</h1>

      {/* Menu simple */}
      <div className="flex space-x-4 justify-center mb-8">
        <button
          onClick={() => setActiveTab("airportQuery")}
          className={`px-4 py-2 rounded ${
            activeTab === "airportQuery" ? "bg-blue-600 text-white" : "bg-white"
          }`}
        >
          Query Airport
        </button>
        <button
          onClick={() => setActiveTab("runwayTypes")}
          className={`px-4 py-2 rounded ${
            activeTab === "runwayTypes" ? "bg-blue-600 text-white" : "bg-white"
          }`}
        >
          RunwayTypes
        </button>
        <button
          onClick={() => setActiveTab("latitude")}
          className={`px-4 py-2 rounded ${
            activeTab === "latitude" ? "bg-blue-600 text-white" : "bg-white"
          }`}
        >
          Latitude
        </button>
        <button
          onClick={() => setActiveTab("topCountries")}
          className={`px-4 py-2 rounded ${
            activeTab === "topCountries" ? "bg-blue-600 text-white" : "bg-white"
          }`}
        >
          TopCountry
        </button>
      </div>

      {/* Contenu conditionnel */}
      <div className="max-w-4xl mx-auto">
        {activeTab === "airportQuery" && <AirportQuery />}
        {activeTab === "runwayTypes" && <RunwayTypes />}
        {activeTab === "latitude" && <LatitudeReportComponent />}
        {activeTab === "topCountries" && <Countries />}
      </div>
    </div>
  );
}
