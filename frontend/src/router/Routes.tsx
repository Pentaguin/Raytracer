import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import HomePage from "../pages/HomePage";

export const router = createBrowserRouter([
    {
        path: '/',
        Component: App,
        children: [
            {index: true, Component: HomePage},
        ]
    },
]);