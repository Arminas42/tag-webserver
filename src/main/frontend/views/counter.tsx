import React, { useState } from 'react';
// import { Button } from '@vaadin/react-components/Button.js';
import { HorizontalLayout } from '@vaadin/react-components/HorizontalLayout.js';
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"

export default function Counter() {
  const [counter, setCounter] = useState(0);

  return (
    <HorizontalLayout theme="spacing" style={{ alignItems: 'baseline' }}>
      <Button onClick={() => setCounter(counter + 1)}>Button</Button>
      <p>Clicked {counter} times</p>
      <div className="flex justify-center items-center min-h-screen bg-gray-50 p-6">
        <Card className="w-[350px] shadow-lg">
          <CardHeader>
            <CardTitle className="text-xl font-semibold">Welcome</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="mb-4 text-gray-600">
              This is a simple card component using shadcn/ui.
            </p>
            <Button className="w-full">Click Me</Button>
          </CardContent>
        </Card>
      </div>
    </HorizontalLayout>
  );
}