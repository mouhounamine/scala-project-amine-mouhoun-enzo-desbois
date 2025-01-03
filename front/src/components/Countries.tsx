"use client";

import { useEffect, useState } from "react";
import { getTopCountries } from "@/app/services/api";
import { Country } from "@/app/models/types";
import TopCountry from "./TopCountry";

export default function Countries() {
  const [topCountries, setTopCountries] = useState<Country[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    getTopCountries()
      .then(data => {
        setTopCountries(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  return (
    <div className="w-full flex justify-center">
        <div className="w-[60%] flex-wrap">
            <h1>10 countries with highest number of airports (with count) and countries  with lowest number of airports.</h1>
        </div>
      {loading ? (
        <div className="flex flex-col justify-center items-center space-x-2">
          {/* Icone de chargement */}
          <svg
            className="animate-spin h-8 w-8 text-blue-600"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <circle cx="12" cy="12" r="10" stroke="currentColor" strokeOpacity="0.3" />
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M4 12a8 8 0 018-8 8 8 0 010 16 8 8 0 01-8-8z"
            />
          </svg>
          <span>Chargement...</span>
        </div>
      ) : error ? (
        <p className="text-red-500">Erreur : {error}</p>
      ) : (
        <ul className="mt-4 space-y-4 w-full max-w-xl">
          {topCountries.map((country, index) => (
            <TopCountry key={index} country={country} />
          ))}
        </ul>
      )}
    </div>
  );
}
