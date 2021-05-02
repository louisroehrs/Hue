import {React, useState} from "react";

const DataFieldGraph = ({
                          id,
                          label,
                          value,
                          barcolor
                        }) => {
  return (
    <div className={"datafield--field"}>
      <div className={"datafield--string"}>{value == null ? "na":
        <div className={"datafield--graph-bar"} style={{"width":value*1.5+"px","background-color":barcolor}}>{value}</div>
      }
      </div>
      <div className={"datafield--label"}>{label}</div>
    </div>
  )
}

export default DataFieldGraph;