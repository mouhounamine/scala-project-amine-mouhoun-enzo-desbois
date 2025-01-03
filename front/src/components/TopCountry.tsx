"use client";

import { Country } from "@/app/models/types";

interface TopCountryProps {
  country: Country;
}

export default function TopCountry({ country }: TopCountryProps) {
  return (
    <li className="flex items-center justify-between bg-gray-100 p-4 rounded shadow">
      <span className="font-semibold">{country.code}</span>
      <span className="text-blue-600">{country.value.toLocaleString()}</span>
    </li>
  );
}
