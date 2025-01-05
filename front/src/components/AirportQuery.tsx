"use client";

import { useState } from "react";
import { getAirportsByCountry } from "@/app/services/api";
import { Airport } from "@/app/models/types";

export default function AirportQuery() {
  const [inputValue, setInputValue] = useState<string>("");
  const [airports, setAirports] = useState<Airport[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const handleSearch = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getAirportsByCountry(inputValue);
      setAirports(data);
    } catch (err: any) {
      setError(err.message);
      setAirports([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-4 border rounded bg-white shadow">
      <h2 className="text-xl font-bold mb-2">Rechercher des aéroports par pays ou code ISO</h2>

      <div className="flex space-x-2 mb-4">
        <input
          type="text"
          className="border p-2 flex-1"
          placeholder="Ex: France ou FR"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
        />
        <button
          className="bg-blue-500 text-white px-4 py-2 rounded"
          onClick={handleSearch}
          disabled={loading}
        >
          {loading ? "Recherche..." : "Chercher"}
        </button>
      </div>

      {error && <p className="text-red-500">Erreur : {error}</p>}

      {/* Résultats */}
      {airports.length > 0 && (
        <ul className="space-y-2">
          {airports.map((airport) => (
            <li key={airport.id} className="p-2 border rounded">
              <strong>{airport.name}</strong> — {airport.ident} — {airport.iso_country}
            </li>
          ))}
        </ul>
      )}

      {airports.length === 0 && !loading && !error && (
        <p className="text-gray-500">Aucun aéroport affiché pour l’instant.</p>
      )}
    </div>
  );
}
