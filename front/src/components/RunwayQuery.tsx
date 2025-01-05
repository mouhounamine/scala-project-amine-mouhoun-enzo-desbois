"use client";

import { useState, useEffect } from "react";
import { getRunwayTypes } from "@/app/services/api";
import { RunwayType } from "@/app/models/types";

interface RunwayData {
  [countryCode: string]: string[]; // Clé : Code du pays, Valeurs : Types de runway
}

export default function RunwayTypes() {
  const [runwayData, setRunwayData] = useState<RunwayType>({});
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    getRunwayTypes()
      .then((data) => {
        setRunwayData(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  return (
    <div className="p-4 border rounded bg-white shadow">
      <h2 className="text-xl font-bold mb-4">Runway Types par Pays</h2>

      {loading && (
        <p className="text-gray-500 animate-pulse">Chargement des données...</p>
      )}
      {error && <p className="text-red-500">Erreur : {error}</p>}

      {!loading && !error && (
        <div>
          {Object.entries(runwayData).map(([countryCode, runways]) => (
            <div key={countryCode} className="mb-6">
              <h3 className="text-lg font-semibold">{countryCode}</h3>
              <ul className="list-disc pl-6">
                {runways
                  .filter((runway) => runway.trim() !== "") // Supprimer les valeurs vides
                  .map((runway, index) => (
                    <li key={index}>{runway}</li>
                  ))}
              </ul>
            </div>
          ))}
        </div>
      )}

      {!loading && !error && Object.keys(runwayData).length === 0 && (
        <p className="text-gray-500">Aucune donnée disponible.</p>
      )}
    </div>
  );
}
