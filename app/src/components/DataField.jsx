import {React, useState} from "react";

const DataField = ({
                  id,
                  label,
                  value,
                }) => {

  return (
    <div className={"datafield--field"}>
      <div className={"datafield--string"}>{value == null ? "na":value}</div>
      <div className={"datafield--label"}>{label}</div>
    </div>
  )
}

export default DataField;