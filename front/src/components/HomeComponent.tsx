"use client";

import Countries from "./Countries";

export default function HomeComponent() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen p-8">
      <h1 className="text-2xl font-bold mb-8">Bienvenue à Worldwide</h1>
      <Countries />
    </div>
  );
}
