import React, { useState, useEffect } from 'react';
import ReactDataGrid from "react-data-grid";
import { Toolbar, Data } from "react-data-grid-addons";
import 'bootstrap/dist/css/bootstrap.css';

const defaultColumnProperties = {
  sortable: true,
  filterable: true,
}

const columns = [
  { key: "team", name: "Team" },
  { key: "year", name: "Year"},
  { key: "wins", name: "Wins"},
  { key: "loses", name: "Loses"},
  { key: "winStreak", name: "Winning Streak"},
  { key: "loosingStreak", name: "Loosing Streak"},
  { key: "highestScore", name: "Highest Score"},

].map(c => ({...c, ...defaultColumnProperties}));

const sortRows = (initialRows, sortColumn, sortDirection) => rows => {
  const comparer = (a, b) => {
    if (sortDirection === "ASC") {
      return a[sortColumn] > b[sortColumn] ? 1 : -1;
    } else if (sortDirection === "DESC") {
      return a[sortColumn] < b[sortColumn] ? 1 : -1;
    }
  };
  return sortDirection === "NONE" ? initialRows : [...rows].sort(comparer);
};

const selectors = Data.Selectors;

const handleFilterChange = filter => filters => {
  const newFilters = { ...filters };
  if (filter.filterTerm) {
    newFilters[filter.column.key] = filter;
  } else {
    delete newFilters[filter.column.key];
  }
  return newFilters;
};

function getRows(rows, filters) {
  return selectors.getRows({ rows, filters });
}

function App() {
  const [rows, setRows] = useState(Array);
  const [filters, setFilters] = useState({});
  const filteredRows = getRows(rows, filters);

  useEffect(() => {
    fetch("/get_report")
      .then(res => res.json())
      .then(
        (result) => {
          setRows(result.report);
        },
        (error) => {
        }
      )  
  }, []);

  return (
    <ReactDataGrid
        columns={columns}
        rowGetter={i => filteredRows[i]}
        rowsCount={filteredRows.length}
        minHeight={800}
        onGridSort={(sortColumn, sortDirection) =>
          setRows(sortRows(filteredRows, sortColumn, sortDirection))
        }
        toolbar={<Toolbar enableFilter={true} />}
        onAddFilter={filter => setFilters(handleFilterChange(filter))}
        onClearFilters={() => setFilters({})}
      />
  );
}

export default App;
