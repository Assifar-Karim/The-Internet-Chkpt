import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import { BrowserRouter } from "react-router-dom";
import axios from "axios";
import  {UserProvider} from './context/UserContext'

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <React.StrictMode>
       <UserProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
       </UserProvider>
    </React.StrictMode>
);
