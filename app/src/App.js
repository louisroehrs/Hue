import { BrowserRouter, useState, useEffect} from 'react'
import {useSelector, useDispatch} from 'react-redux'
import logo from './logo.svg';
import './App.css';
import useHueBridgeService from "./features/huebridge/hueBridgeService";
import useOutletService  from './features/outlets/outletService.js';

import Light from "./components/Light";
import Sensor from "./components/Sensor";
import {RGBtoHSV} from "./algos/hsvandrgb";
import Outlet from "./components/Outlet";
import {current} from "@reduxjs/toolkit";

function App() {

  const [currentMode,setCurrentMode] = useState("LIGHTS");

  const lights = useSelector( (state) => state.hueBridge.lights);
  const sensors = useSelector( (state) => state.hueBridge.sensors);

  const outlets = useSelector((state) => state.outlets.outlets);

  const hueBridgeLoading = useSelector ((state) => state.hueBridgeLoading);

  const dispatch = useDispatch();
  const hueBridgeService = useHueBridgeService(dispatch);
  const outletService = useOutletService(dispatch);

  useEffect( () => {
      hueBridgeService.fetchLights();
      hueBridgeService.fetchSensors();
  },[]);

  const lightClicked = (id,on) => {
    hueBridgeService.turnLightOn(id,on);
  }

  const lightColorChange = (id, color) => {
    const {h,s,v} = RGBtoHSV(color.r,color.g,color.b);

    hueBridgeService.setLightColor(id, {
      bri:Math.floor(v*254),
      hue:Math.floor(h*65536),
      sat:Math.floor(s*255)
    });
  }

  const handleOnOutletClick = (outlet) => {
    if (outlet.exclusive) {
      outletService.toggleCallForOn(outlet);
    } else {
      outletService.setOutlet(outlet.id,!outlet.outletOn);
    }
    outletService.fetchOutlets();
  }

  return (
    <div className="App">
      <div className={"grid"}>
        <div className={"gridheader"}>
          <div className={"gridheaderend"}/>
          <div className={"gridheadergap"}/>
          <div className={"gridheaderbutton"}/>
          <div className={"gridtitle"}>{currentMode}</div>
          <div className={"gridheadergap"}/>
        </div>
      </div>
      <div className={"scrollinglist"}
           style={{"height":(document.body.clientHeight-90)+"px"}}>

        <div className={"grid"}>
          {
            (currentMode === "LIGHTS" &&
              lights.map(light => (
                <>
                  <div className={"gridlefter"}/>
                  <Light
                    id={light.id}
                    key={light.id}
                    name={light.name}
                    on={light.state.on}
                    bri={light.state.bri}
                    hue={light.state.hue}
                    sat={light.state.sat}
                    onClick={() => lightClicked(light.id, !light.state.on)}
                    onColorChange={(color) => lightColorChange(light.id, color)}
                  />
                </>
              ))
            )
            || (
              currentMode === "SENSORS" &&
              sensors.map(sensor => (
                <>
                  <div className={"gridlefter"}/>
                  <Sensor
                    key={sensor.id}
                    id={sensor.id}
                    name={sensor.name}
                    type={sensor.type}
                    modelid={sensor.modelid}
                    manufacturername={sensor.manufacturername}
                    uniqueid={sensor.uniqueid}
                    battery = {sensor.config ? sensor.config.battery:"na"}
                  />
                </>
              ))
            )
            || (
              currentMode === "CHAIRS" &&
              outlets.map(outlet => (
                <>
                  <div className={"gridlefter"}/>
                  <Outlet
                    key = {outlet.id}
                    id={outlet.id}
                    label={outlet.name}
                    callForOn = {outlet.callForOn}
                    outletOn = {outlet.outletOn}
                    onClick = {() => handleOnOutletClick(outlet)}
                  />
                </>
              ))
            )
          }
        </div>
      </div>
      <div className={"grid"}>
        <div className={"gridfooter"}>
          <div onClick={() => setCurrentMode("SENSORS")} className={"gridfooterbutton"}>SENSORS</div>
          <div onClick={() => setCurrentMode("LIGHTS")} className={"gridfooterbutton"}>LIGHTS</div>
          <div onClick={() => setCurrentMode("CHAIRS")} className={"gridfooterbutton"}>CHAIRS</div>
        </div>
      </div>
    </div>

  );
}

export default App;
