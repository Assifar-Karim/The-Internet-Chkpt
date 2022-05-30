import { Switch, Route } from "react-router-dom";
import LoginCallback from './components/LoginCallback/LoginCallback'
import { routes } from "./Routes";
import { QueryClient, QueryClientProvider } from "react-query";

const queryClient = new QueryClient();

function App() {
  return (
    <div className="App">
      <QueryClientProvider client={queryClient}>
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
      </QueryClientProvider>
    </div>
  );
}

export default App;