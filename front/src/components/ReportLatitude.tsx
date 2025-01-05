"use client";

import { useState, useEffect } from "react";
import { getLatitudeReport } from "@/app/services/api";
import { LatitudeReport } from "@/app/models/types";

export default function LatitudeReportComponent() {
  const [latReports, setLatReports] = useState<LatitudeReport[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    getLatitudeReport()
      .then((data) => {
        setLatReports(data); // Stocke les données converties
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message); // Capture l'erreur
        setLoading(false);
      });
  }, []);

  return (
    <div className="p-4 border rounded bg-white shadow">
      <h2 className="text-xl font-bold mb-4">Rapport Latitude</h2>

      {loading && (
        <p className="text-gray-500 animate-pulse">Chargement des données...</p>
      )}
      {error && <p className="text-red-500">Erreur : {error}</p>}

      {!loading && !error && (
        <ul className="space-y-2">
          {latReports.map((item) => (
            <li key={item.id} className="p-2 border rounded">
              <strong>{item.id}</strong> — Latitude : {item.value}
            </li>
          ))}
        </ul>
      )}

      {!loading && !error && latReports.length === 0 && (
        <p className="text-gray-500">Aucune donnée disponible.</p>
      )}
    </div>
  );
}
