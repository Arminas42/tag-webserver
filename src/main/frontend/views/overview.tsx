import React, { useState, useEffect } from 'react';
import { HorizontalLayout } from '@vaadin/react-components/HorizontalLayout.js';
import { DataTable } from "@/components/ui/data-table"
import { columns } from "@/components/table-components/tag-columns"
import { TagService } from "Frontend/generated/endpoints";
import TagDetection from "Frontend/generated/com/kuaprojects/rental/tag/TagDetection.js";
import AppLayout from "@/components/layout/AppLayout";
import { Button } from "@/components/ui/button";

export default function Overview() {
    const [tags, setTags] = useState<TagDetection[]>([]);
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(5);
    const [totalCount, setTotalCount] = useState(0);

      useEffect(() => {
        const fetchTags = async () => {
          try {
            const result = await TagService.getPagedDetections(pageSize, page);

            if (Array.isArray(result)) {
              // ✅ Filter out undefined values
              const safeResult: TagDetection[] = result.filter(
                (tag): tag is TagDetection => tag !== undefined
              );
              console.log("Fetched tags:", safeResult);
              setTags(safeResult);
              // If the API returns total count, set it here
              // setTotalCount(result.totalCount);
            } else {
              console.warn("No tags returned or invalid format:", result);
              setTags([]);
            }
          } catch (error) {
            console.error("Error fetching tags:", error);
            setTags([]);
          }
        };

        fetchTags();
      }, [page, pageSize]);


  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  const handlePageSizeChange = (newPageSize: number) => {
    setPageSize(newPageSize);
    setPage(0); // Reset to first page when changing page size
  };

  return (
        <AppLayout onLogout={() => {/* TODO: implement logout */}}>
          <div className="w-full">
            <div className="w-1/2 mx-auto">
              <DataTable 
                className="w-full" 
                columns={columns} 
                data={tags}
                pageSize={pageSize}
                onPageSizeChange={handlePageSizeChange}
                currentPage={page}
                onPageChange={handlePageChange}
                canPreviousPage={page > 0}
                canNextPage={tags.length === pageSize}
              />
            </div>
          </div>
        </AppLayout>
      );
}