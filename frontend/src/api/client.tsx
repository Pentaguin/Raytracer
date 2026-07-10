import { DefaultApi } from "./gen/api";
import { Configuration } from "./gen/configuration";

const configuration = new Configuration({
    basePath: "/api/v1",
});

export const apiClient = new DefaultApi(configuration);