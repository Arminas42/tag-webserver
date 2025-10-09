"use client"

import {
  ColumnDef,
  flexRender,
  getCoreRowModel,
  useReactTable,
  getPaginationRowModel,
} from "@tanstack/react-table"

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Button } from "@/components/ui/button"

interface DataTableProps<TData, TValue> {
  columns: ColumnDef<TData, TValue>[]
  data: TData[]
  className?: string
  pageSize?: number
  onPageSizeChange?: (pageSize: number) => void
  currentPage?: number
  onPageChange?: (page: number) => void
  canPreviousPage?: boolean
  canNextPage?: boolean
}

export function DataTable<TData, TValue>({
  columns,
  data,
  className,
  pageSize,
  onPageSizeChange,
  currentPage = 0,
  onPageChange,
  canPreviousPage = false,
  canNextPage = false,
}: DataTableProps<TData, TValue>) {
  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    manualPagination: true, // Disable internal pagination for server-side
    pageCount: -1, // Unknown page count for server-side pagination
  })

  const rows = table.getRowModel().rows
  const emptyRowCount = Math.max(0, (pageSize || 10) - rows.length)

  return (
  <div className={className}>
    <div className="overflow-hidden rounded-md border">
      <Table className="table-fixed w-full">
        <colgroup>
          {table.getAllLeafColumns().map((column) => (
            <col key={column.id} style={{ width: column.getSize() }} />
          ))}
        </colgroup>
        <TableHeader>
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => {
                return (
                  <TableHead
                    key={header.id}
                    className="overflow-hidden text-ellipsis whitespace-nowrap"
                  >
                    {header.isPlaceholder
                      ? null
                      : flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                  </TableHead>
                )
              })}
            </TableRow>
          ))}
        </TableHeader>
        <TableBody>
          {rows?.length ? (
            rows.map((row) => (
              <TableRow
                key={row.id}
                data-state={row.getIsSelected() && "selected"}
              >
                {row.getVisibleCells().map((cell) => (
                  <TableCell
                    key={cell.id}
                    className="overflow-hidden text-ellipsis whitespace-nowrap"
                  >
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </TableCell>
                ))}
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell colSpan={columns.length} className="h-24 text-center">
                No results.
              </TableCell>
            </TableRow>
          )}

          {rows?.length > 0 && emptyRowCount > 0 &&
            Array.from({ length: emptyRowCount }).map((_, index) => (
              <TableRow key={`empty-${index}`} aria-hidden>
                {Array.from({ length: columns.length }).map((__, cIdx) => (
                  <TableCell key={`empty-${index}-${cIdx}`}>&nbsp;</TableCell>
                ))}
              </TableRow>
            ))}
        </TableBody>
      </Table>
      <div className="flex items-center justify-between border-t bg-card px-4 py-3">
        {/* Page size selector */}
        {onPageSizeChange && (
          <div className="flex items-center gap-2">
            <span className="text-sm text-muted-foreground">Page size:</span>
            <select 
              value={pageSize || 5}
              onChange={(e) => onPageSizeChange(Number(e.target.value))}
              className="px-2 py-1 border rounded text-sm"
            >
              <option value={3}>3</option>
              <option value={5}>5</option>
              <option value={10}>10</option>
            </select>
          </div>
        )}
        
        {/* Pagination buttons */}
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => onPageChange?.(currentPage - 1)}
            disabled={!canPreviousPage}
          >
            Previous
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => onPageChange?.(currentPage + 1)}
            disabled={!canNextPage}
          >
            Next
          </Button>
        </div>
      </div>
    </div>
    </div>
  )
}