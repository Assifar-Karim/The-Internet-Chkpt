import LoginCallback from "./components/LoginCallback/LoginCallback";
import AboutUs from "./pages/AboutUs/AboutUs";
import Checkpoints from "./pages/Checkpoints/Checkpoints";
import MainPage from "./pages/MainPage/MainPage";
import MyCheckpoints from "./pages/MyCheckpoints/MyCheckpoints";
import Page404 from "./pages/Page404/Page404";
import SharedCheckpoint from "./pages/SharedCheckpoint/SharedCheckpoint";
import SignIn from "./pages/SignIn/SignIn";
import SignUp from "./pages/SignUp/SignUp";

export const routes = [
  { title: "", segment: "/", component: MainPage, readerToNav: false },
  {
    title: "Checkpoints",
    segment: "/checkpoints",
    component: Checkpoints,
    renderToNav: true,
  },
  {
    title: "Shared checkpoint",
    segment: "/checkpoints/:id",
    component: SharedCheckpoint,
    renderToNav: false,
  },
  {
    title: "My checkpoints",
    segment: "/my-checkpoints",
    component: MyCheckpoints,
    renderToNav: true,
  },
  {
    title: "User' checkpoint",
    segment: "/user/:username",
    component: MyCheckpoints,
    renderToNav: false,
  },
  {
    title: "About Us",
    segment: "/about-us",
    component: AboutUs,
    renderToNav: true,
  },
  {
    title: "Sign In",
    segment: "/sign-in",
    component: SignIn,
    renderToNav: false,
  },
  {
    title: "Sign UP",
    segment: "/sign-up",
    component: SignUp,
    renderToNav: false,
  },
  {
    title: "404",
    segment: "/404",
    component: Page404,
    renderToNav: false,
  }
];
