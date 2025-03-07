import {React, BrowserRouter, useState, useEffect, useCallback} from 'react'
import {useSelector, useDispatch} from 'react-redux'
import logo from './logo.svg';
import './App.css';
import useHueBridgeService from "./features/huebridge/hueBridgeService";
import useOutletService  from './features/outlets/outletService.js';
import useChairService from './features/chairs/chairService';

import Light from "./components/Light";
import Sensor from "./components/Sensor";
import Chair from "./components/Chair";
import {RGBtoHSV} from "./algos/hsvandrgb";
import Outlet from "./components/Outlet";
import {current} from "@reduxjs/toolkit";

const App = ({}) =>  {

  const [currentMode,setCurrentMode] = useState("LIGHTS");

  const lights = useSelector( (state) => state.hueBridge.lights);
  const sensors = useSelector( (state) => state.hueBridge.sensors);
  const chairs = useSelector( (state) => state.chairs.chairs)
  const outlets = useSelector((state) => state.outlets.outlets);

  // const modes = ["CHAIRS","LIGHTS","SENSORS","OUTLETS"];
  const modes = ["LIGHTS","SENSORS"];

  const hueBridgeLoading = useSelector ((state) => state.hueBridgeLoading);

  const dispatch = useDispatch();
  const hueBridgeService = useHueBridgeService(dispatch);
  // const outletService = useOutletService(dispatch);
  // const chairService = useChairService(dispatch);

  const [increment,setIncrement] = useState(0);

  useEffect( () => {
    hueBridgeService.fetchLights();
    hueBridgeService.fetchSensors();
    // outletService.fetchOutlets();
    // chairService.fetchChairs();
    const interval=setInterval(()=>{
      hueBridgeService.fetchLights();
      // chairService.fetchChairs();
    },10000)
    return()=>clearInterval(interval)
  },[increment]);


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
    if (!outlet.exclusive) {
//      outletService.setOutlet(outlet.id,!outlet.outletOn);
    }
//    outletService.fetchOutlets();
  }

  const handleOnChairClick = (chair) => {
//    chairService.requestChair(chair.id, !chair.requested);
  }

  let appleWatch =  (() => {
    let a = document.createElement('audio');
    let supportsAAC = !!(a.canPlayType && a.canPlayType('audio/mp4; codecs="mp4a.40.2"').replace(/no/, ''));
    let is_iphone = ['iPhone Simulator', 'iPhone'].includes(navigator.platform)
    return is_iphone && !supportsAAC
  })();

//  appleWatch = true;

  let scroll =0;

//  useEffect( () => {
//    if (appleWatch) {
      // set the scroll to be winright on a button.
//      window.scrollTo(0,800);
//    }
//  })

  const handleWatchScroll = (event) => {
    let newScroll = scroll + (event.target.scrollTop - scroll) * 45;
    newScroll = Math.max(0,newScroll);
    newScroll = Math.min ( 1000, newScroll);
    scroll = newScroll;
    window.scrollTo(0, newScroll);
  }

  // window.addEventListener('scroll',handleWatchScroll);
  return (
    <>
      { appleWatch &&
      <div className="App" >
        <div className="watch">
          <div style={{"height":"54px"}}/>
          <div className={"grid"}>
            {
              (currentMode === "LIGHTS" &&
                [...lights].sort((a, b) => a.name.localeCompare(b.name)).map(light => (
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
                [...sensors].sort((a, b) => a.name.localeCompare(b.name)).map(sensor => (
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
                      battery={sensor.config ? sensor.config.battery : "na"}
                    />
                  </>
                ))
              )
              || (
                currentMode === "CHAIRS" &&
                chairs.map(chair => (
                    <>
                      <div className={"gridlefter"}/>
                      <Chair
                        key={chair.name}
                        name={chair.name}
                        id={chair.name}
                        requested={chair.requested}
                        poweredOn={chair.poweredOn}
                        onClick={() => handleOnChairClick(chair)}
                      />
                    </>
                  )
                )
              ) || (currentMode === "OUTLETS" &&
                outlets.map(outlet => (
                  <>
                    <div className={"gridlefter"}/>
                    <Outlet
                      key={outlet.name}
                      id={outlet.id}
                      label={outlet.name}
                      exclusive={outlet.exclusive}
                      outletOn={outlet.outletOn}
                      onClick={() => handleOnOutletClick(outlet)}
                    />
                  </>
                ))
              )
            }
          </div>
        </div>

        <div className={"grid"} style={{"position":"fixed",top:"0"}}>
          <div className={"gridheader"}>
            <div className={"gridheaderend"}/>
            <div className={"gridheadergap"}/>
            <div className={"gridheaderbutton"}/>
            <div className={"gridtitle"}>{currentMode}</div>
            <div className={"gridheadergap"}/>
          </div>
        </div>

        <div className={"grid"}  style={{position:"sticky",bottom:"0px"}}>
          <div className={"gridfooter"}>
            {modes.reverse().map(mode => (
                <div onClick={() => setCurrentMode(mode)} className={"gridfooterbutton"}>{mode}</div>
              )
            )}

          </div>
        </div>
      </div>
      }

      {
        !appleWatch &&
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
               style={{"height": (document.body.clientHeight - 110) + "px"}}>

            <div className={"grid"}>
              {
                (currentMode === "LIGHTS" &&
                  [...lights].sort((a, b) => a.name.localeCompare(b.name)).map(light => (
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
                  [...sensors].sort((a, b) => a.name.localeCompare(b.name)).map(sensor => (
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
                        battery={sensor.config ? sensor.config.battery : "na"}
                      />
                    </>
                  ))
                )
                || (
                  currentMode === "CHAIRS" &&
                  chairs.map(chair => (
                      <>
                        <div className={"gridlefter"}/>
                        <Chair
                          key={chair.name}
                          name={chair.name}
                          id={chair.name}
                          requested={chair.requested}
                          poweredOn={chair.poweredOn}
                          onClick={() => handleOnChairClick(chair)}
                        />
                      </>
                    )
                  )
                ) || (currentMode === "OUTLETS" &&
                  outlets.map(outlet => (
                    <>
                      <div className={"gridlefter"}/>
                      <Outlet
                        key={outlet.name}
                        id={outlet.id}
                        label={outlet.name}
                        exclusive={outlet.exclusive}
                        outletOn={outlet.outletOn}
                        onClick={() => handleOnOutletClick(outlet)}
                      />
                    </>
                  ))
                )
              }
            </div>
          </div>
          <div className={"grid"}>
            <div className={"gridfooter"}>
              {modes.reverse().map(mode => (
                  <div onClick={() => setCurrentMode(mode)} className={"gridfooterbutton"}>{mode}</div>
                )
              )}

            </div>
          </div>
        </div>
      }
    </>
  )
}

export default App;
