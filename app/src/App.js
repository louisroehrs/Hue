import { BrowserRouter, useState, useEffect} from 'react'
import {useSelector, useDispatch} from 'react-redux'
import logo from './logo.svg';
import './App.css';
import useHueBridgeService from "./features/huebridge/hueBridgeService";
import Light from "./components/Light";
import {RGBtoHSV} from "./algos/hsvandrgb";

function App() {

  const lights = useSelector( (state) => state.hueBridge.lights);
  const hueBridgeLoading = useSelector ((state) => state.hueBridgeLoading);

  const dispatch = useDispatch();
  const hueBridgeService = useHueBridgeService(dispatch);

  useEffect( () => {
    hueBridgeService.fetchLights();
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
            <div className={"gridheader"}></div>
            <div className={"gridtitle"}>LIGHTS</div>
            <div className={"gridheaderbutton"}></div>
            <div className={"gridheaderend"}></div>
        </div>
            <div className={"scrollinglist"}
                 style={{"height":(document.body.clientHeight-90)+"px"}}>

                <div className={"grid"}>
                {lights.map(light => (
                    <>
                        <div className={"gridlefter"}/>
                        <Light
                            name={light.name}
                            on={light.state.on}
                            bri={light.state.bri}
                            hue={light.state.hue}
                            sat={light.state.sat}
                            onClick={ () => lightClicked(light.id, !light.state.on)}
                            onColorChange={ (color) => lightColorChange(light.id, color)}
                        />
                    </>
                ))
                }
                </div>
            </div>
        <div className={"grid"}>
            <div className={"gridfooter"}/>
        </div>
    </div>

  );
}

export default App;
