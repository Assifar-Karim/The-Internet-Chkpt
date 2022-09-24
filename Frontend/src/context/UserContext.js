import React, { useState } from "react";

export const UserContext = React.createContext({});

export const UserProvider = (props) => {
  
  const [loggedUser, setLoggedUser] = useState(null);

  return (
    <UserContext.Provider value={[loggedUser, setLoggedUser]}>
        {props.children}
    </UserContext.Provider>)
};