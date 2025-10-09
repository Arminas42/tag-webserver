import React from 'react';
import AppLayout from "@/components/layout/AppLayout";

export default function Home() {
  return (
    <AppLayout onLogout={() => {/* TODO: implement logout */}}>
      <div className="w-full">
        <div className="w-1/2 mx-auto text-center">
          <h1 className="text-3xl font-bold mb-4">Welcome to Tag Detection System</h1>
          <p className="text-muted-foreground">
            Navigate to Overview to view tag detections or Counter for testing.
          </p>
        </div>
      </div>
    </AppLayout>
  );
}
