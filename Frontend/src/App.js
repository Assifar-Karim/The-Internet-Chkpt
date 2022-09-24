import { Switch, Route } from "react-router-dom";
import LoginCallback from './components/LoginCallback/LoginCallback'
import { routes } from "./Routes";
import { QueryClient, QueryClientProvider } from "react-query";
import useUser from "./hooks/useUser";
import useRefreshToken from "./hooks/useRefreshToken";
import { useEffect } from "react";

const queryClient = new QueryClient();

function App() {
  const user = useUser()[0];
  const refresh = useRefreshToken();
  useEffect(() => {
    if(!user && localStorage.getItem("token"))
    {
      refresh();
    }
  },[]);
  
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