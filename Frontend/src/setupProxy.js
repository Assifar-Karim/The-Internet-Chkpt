const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = app => {
    app.use(
        "/back",
        createProxyMiddleware({
            target: "http://localhost:8080",
            changeOrigin: true,
            pathRewrite: {
                "/back": ""
            }
        })
    );
    app.use(
        "/oauth2",
        createProxyMiddleware({
            target: "http://localhost:8080",
            changeOrigin: true
        })
    );
};