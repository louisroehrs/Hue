import {React, useState} from "react";
import Drawer from './Drawer';
import DataField from "./DataField";
import DataFieldGraph from "./DataFieldGraph";

const Sensor = ({
                  id,
                  name,
                  type,
                  modelid,
                  manufacturername,
                  battery,
                  uniqueid,
                  onClick,
               }) => {

  const [drawerOpen, setDrawerOpen] = useState(false);

  const openDetails = (event) => {
    setDrawerOpen(!drawerOpen);
    event.stopPropagation();
  }

  const batteryColor = () => {
    if (battery == null) return "white";
    if (battery > 70) return "green";
    if (battery > 30) return "rgb(184,175,58)";
    return "red"
  }

  return (
    <>
      <div className ="griditem"
           onClick={openDetails}>
        <div className={"griditemgap"}/>
        <div className= {"on"} style={{"background-color":batteryColor()}}>&nbsp;</div>
        <div className={"griditemgap"}/>
        <div className="name">{name}</div>
        <Drawer open={drawerOpen}>
          <div style={{color:"white"}}>
            <DataField label={"id"} value={id}/>
            <DataField label={"modelid"} value={modelid}/>
            <DataField label={"type"} value={type}/>
            <DataField label={"manufacturername"} value={manufacturername}/>
            <DataFieldGraph label={"battery"} value={battery} barcolor={batteryColor()}/>
          </div>
        </Drawer>
      </div>
    </>
  )
}

export default Sensor;