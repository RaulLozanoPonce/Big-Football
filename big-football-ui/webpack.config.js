const path = require("path");

const MiniCssExtractPlugin = require("mini-css-extract-plugin");

const webpack = require("webpack");

const HtmlWebpackPlugin = require("html-webpack-plugin")

module.exports = function(_env, argv) {
    const isProduction = argv.mode === "production";
    const isDevelopment = !isProduction;

    return {
        devtool: isDevelopment && "cheap-module-source-map",
        entry: {
            'home': './src/home.js',
            'competition': './src/competition.js',
            'season': './src/season.js',
            'team': './src/team.js',
            'statistics': './src/statistics.js',
            'lineups': './src/lineups.js'
        },
        output: {
            path: "/Users/raull/IdeaProjects/big-football/big-football/src/main/resources/public",
            filename: "assets/js/[name].[contenthash:8].js",
            publicPath: "./"
        },
        module: {
            rules: [
                {
                    test: /\.jsx?$/,
                    exclude: /node_modules/,
                    use: {
                        loader: "babel-loader",
                        options: {
                            cacheDirectory: true,
                            cacheCompression: false,
                            envName: isProduction ? "production" : "development"
                        }
                    }
                },
                {
                    test: /\.css$/,
                    use: [
                        isProduction ? MiniCssExtractPlugin.loader : "style-loader",
                        "css-loader"
                    ]
                },
                {
                    test: /\.(png|jpg|gif)$/i,
                    use: {
                        loader: "url-loader",
                        options: {
                            limit: 8192,
                            name: "static/media/[name].[hash:8].[ext]"
                        }
                    }
                },
                {
                    test: /\.(eot|otf|ttf|woff|woff2)$/,
                    loader: require.resolve("file-loader"),
                    options: {
                        name: "static/media/[name].[hash:8].[ext]"
                    }
                }
            ]
        },
        resolve: {
            extensions: [".js", ".jsx"]
        },
        plugins: [
            isProduction &&
            new MiniCssExtractPlugin({
                filename: "assets/css/[name].[contenthash:8].css",
                chunkFilename: "assets/css/[name].[contenthash:8].chunk.css"
            }),
            new webpack.DefinePlugin({
                "process.env.NODE_ENV": JSON.stringify(
                    isProduction ? "production" : "development"
                )
            }),
            new HtmlWebpackPlugin({
                filename: 'home.html',
                template: 'public/index.html',
                chunks: ['home']
            }),
            new HtmlWebpackPlugin({
                filename: 'competition.html',
                template: 'public/index.html',
                chunks: ['competition']
            }),
            new HtmlWebpackPlugin({
                filename: 'team.html',
                template: 'public/index.html',
                chunks: ['team']
            }),
            new HtmlWebpackPlugin({
                filename: 'season.html',
                template: 'public/index.html',
                chunks: ['season']
            }),
            new HtmlWebpackPlugin({
                filename: 'statistics.html',
                template: 'public/index.html',
                chunks: ['statistics']
            }),
            new HtmlWebpackPlugin({
                filename: 'lineups.html',
                template: 'public/index.html',
                chunks: ['lineups']
            })
        ].filter(Boolean)
    };
};