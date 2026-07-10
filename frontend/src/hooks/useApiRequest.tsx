import { useState } from "react";
import { apiClient } from "../api/client";
import type { RenderRequest } from "../api/gen/api";

export const useApiRequest = () => {
    const [data, setData] = useState<string | null>(null);
    const [status, setStatus] = useState("idle");

    const renderImage = async (request: RenderRequest) => {
        setStatus("loading");

        try {
            const response = await apiClient.renderImage(request, {
                responseType: "blob",
            });
            const blob = new Blob([response.data], { type: "image/png" });
            const imageUrl = URL.createObjectURL(blob);

            setData(imageUrl);
            setStatus("success");

            return imageUrl;

        } catch (error) {
            setStatus("error");
            throw error;
        }
    };

    return {
        renderImage,
        data,
        status
    };
};