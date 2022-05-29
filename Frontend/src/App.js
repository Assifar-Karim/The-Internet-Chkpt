import { Switch, Route } from "react-router-dom";

import { routes } from "./Routes";

function App() {
  return (
    <div className="App">
      <Switch>
        {routes.map((route, index) => (
          <Route
            exact
            key={index}
            path={route.segment}
            component={route.component}
          />
        ))}
      </Switch>
    </div>
  );
}

export default App;
