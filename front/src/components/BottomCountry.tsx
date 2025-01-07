"use client";

import { Country } from "@/app/models/types";

interface BottomCountryProps {
  country: Country;
}

export default function BottomCountry({ country }: BottomCountryProps) {
  return (
    <li className="flex items-center justify-between bg-red-200 p-4 rounded shadow">
      <span className="font-semibold">{country.code}</span>
      <span className="text-blue-600">{country.value.toLocaleString()}</span>
    </li>
  );
}
