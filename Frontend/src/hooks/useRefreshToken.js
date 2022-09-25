import axios from "axios";
import useUser from "./useUser";

const useRefreshToken = () => {
    const setUser = useUser()[1];
    const refresh = async () => {
        const response = await axios.get("/back/jwt", {headers: {Authorization: `Bearer ${localStorage.getItem("token")}`}});
        setUser(prev => {
            return {...prev, accessToken: response.data["access_token"]};
        });
        return response.data["access_token"];
    }

    return refresh;
};

export default useRefreshToken;