export interface Country {
    code: string; 
    value: number;
  }

export interface Airport {
  id: number,
  ident: string,
  type: string,
  name: string,
  latitude_deg: number,
  longitude_deg: number,
  elevation_ft: number | null,
  continent: string,
  iso_country: string,
  iso_region: string,
  municipality: string | null,
  scheduled_service: string,
  gps_code: string | null,
  iata_code: string | null,
  local_code: string | null,
  home_link: string | null,
  wikipedia_link: string | null,
  keywords: string | null
}

export interface RunwayType {
  [countryCode: string]: string[];
}

export interface LatitudeReport {
  id: string;
  value: number; 
}
