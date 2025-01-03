import { Country } from "../models/types";

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
