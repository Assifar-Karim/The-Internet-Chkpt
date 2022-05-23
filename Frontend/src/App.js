import { Route, Routes } from "react-router-dom";
import { routes } from "./Routes";

function App() {
  return (
    <div className="App">
      <Routes>
        {routes.map((route, index) => (
          <Route key={index} path={route.segment} element={route.component()} />
        ))}
      </Routes>
    </div>
  );
}

export default App;
