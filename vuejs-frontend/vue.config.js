// module.exports = {
//     devServer: {
//         proxy: {
//             '/api': {
//                 target: 'http://client-server:8081',  // Replace with the URL of your Spring Boot backend
//                 changeOrigin: true
//             },
//             '/oauth2': {
//                 target: 'http://client-server:8081',  // Replace with the URL of your Spring Boot backend
//                 changeOrigin: true
//             }
//         }
//     }
// };
module.exports = {
    outputDir: '..\\client-server\\src\\main\\resources\\static'
};