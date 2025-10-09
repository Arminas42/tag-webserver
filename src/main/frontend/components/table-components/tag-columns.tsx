"use client"

import { ColumnDef } from "@tanstack/react-table"
import TagDetection from "Frontend/generated/com/kuaprojects/rental/tag/TagDetection.js";

export const columns: ColumnDef<TagDetection>[] = [
  {
    id: "detectionTime",
    accessorKey: "detectionTime",
    header: "Time",
    cell: ({ getValue }) => {
      const dateTime = getValue() as string;
      if (!dateTime) return "";
      const date = new Date(dateTime);
      return date.toLocaleTimeString('en-GB', { 
        hour12: false,
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    },
  },
  {
    accessorKey: "tagCode",
    header: "Tag name",
  },
  {
    id: "detectionDate",
    accessorKey: "detectionTime",
    header: "Date",
    cell: ({ getValue }) => {
      const dateTime = getValue() as string;
      if (!dateTime) return "";
      const date = new Date(dateTime);
      return date.toLocaleDateString();
    },
  },
  {
    accessorKey: "id",
    header: "ID",
  },
]