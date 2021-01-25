import { BrowserRouter, useState, useEffect} from 'react'
import {useSelector, useDispatch} from 'react-redux'
import logo from './logo.svg';
import './App.css';
import useHueBridgeService from "./features/huebridge/hueBridgeService";
import Light from "./components/Light";
import Sensor from "./components/Sensor";
import {RGBtoHSV} from "./algos/hsvandrgb";
import {current} from "@reduxjs/toolkit";

function App() {

  const [currentMode,setcurrentMode] = useState("LIGHTS");

  const lights = useSelector( (state) => state.hueBridge.lights);
  const sensors = useSelector( (state) => state.hueBridge.sensors);

  const hueBridgeLoading = useSelector ((state) => state.hueBridgeLoading);

  const dispatch = useDispatch();
  const hueBridgeService = useHueBridgeService(dispatch);

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

  return (
    <div className="App">
      <div className={"grid"}>
        <div className={"gridheader"}>
          <div className={"gridheaderend"}></div>
          <div className={"gridheadergap"}></div>
          <div className={"gridheaderbutton"}></div>
          <div className={"gridtitle"}>{currentMode}</div>
          <div className={"gridheadergap"}></div>
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
          }
        </div>
      </div>
      <div className={"grid"}>
        <div className={"gridfooter"}>
          <div onClick={() => setcurrentMode("SENSORS")} className={"gridfooterbutton"}>SENSORS</div>
          <div onClick={() => setcurrentMode("LIGHTS")} className={"gridfooterbutton"}>LIGHTS</div>
          <div className={"gridfooterbutton"}>CHAIRS</div>
        </div>
      </div>
    </div>

  );
}

export default App;
