import { Country, Airport, RunwayType, LatitudeReport } from "../models/types";

export const BASE_URL = "http://localhost:9000";

export async function getTopCountries(): Promise<Country[]> {
  try {
    const response = await fetch(`${BASE_URL}/reports/topCountries`);
    if (!response.ok) {
      throw new Error(`Erreur HTTP : ${response.status}`);
    }
    const data: [string, number][] = await response.json();
    return data.map(([code, value]) => ({ code, value })); // Transformer le tableau en objets structurés
  } catch (error) {
    console.error("Erreur lors de l'appel à l'API getTopCountries :", error);
    throw error;
  }
}
/**
 * Récupère les aéroports pour un pays ou code ISO (ex. /query/France ou /query/FR)
 */
export async function getAirportsByCountry(countryOrCode: string): Promise<Airport[]> {
  const res = await fetch(`http://localhost:9000/query/${countryOrCode}`);
  if (!res.ok) {
    throw new Error(`Erreur HTTP : ${res.status}`);
  }
  return res.json();
}

/**
 * Récupère les runwayTypes ( /reports/runwayTypes )
 */
export async function getRunwayTypes(): Promise<RunwayType> {
  const res = await fetch("http://localhost:9000/reports/runwayTypes");
  if (!res.ok) {
    throw new Error(`Erreur HTTP : ${res.status}`);
  }
  return res.json();
}

/**
 * Récupère la liste latitude ( /reports/latitude )
 */
export async function getLatitudeReport(): Promise<LatitudeReport[]> {
  const res = await fetch("http://localhost:9000/reports/latitude");
  if (!res.ok) {
    throw new Error(`Erreur HTTP : ${res.status}`);
  }

  const rawData: [string, number][] = await res.json();
  return rawData.map(([id, value]) => ({ id, value }));
}
