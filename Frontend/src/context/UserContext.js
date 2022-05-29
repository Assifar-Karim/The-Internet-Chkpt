import React, { useState } from "react";

export const UserContext = React.createContext();

export const UserProvider = (props) => {
  const token = localStorage.getItem("token")

  const [loggedUser, setLoggedUser] = useState(token ? true : false);

return (
<UserContext.Provider value={[loggedUser, setLoggedUser]}>
    {props.children}
</UserContext.Provider>
)};