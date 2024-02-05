/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  darkMode: "class",
  theme: {
    screens: {
      "xlsm": "460px" /* extra light small (small is 640px) */,
      "lmd": "700px" /* light medium (medium is 768px) */,
      "llg": "875px" /* light large (large is 1024px) */,
      "l2xl": "1365px" /* light 2 extra large (2 extra large is 1536px) */,
    },
    extend: {
      colors: {
        /* Primary */
        "primary": "#1a8cd8",

        /* White */
        "white-0": "#ffffff",

        /* Gray */
        "gray-0": "rgba(15, 20, 25, .1)",
        "gray-1": "rgba(239, 243, 244, .1)",
        "gray-2": "rgb(153, 153, 153)",
        "gray-3": "#e7e9ea",
        "gray-4": "#536471",
        "gray-5": "#71767b",
        "gray-6": "#f7f9f9",
        "gray-7": "#eff1f1",
        "gray-8": "#e7e7e8",
        "gray-9": "rgb(207, 217, 222)",
        "gray-10": "#eff3f4",
        "gray-11": "rgb(239, 243, 244)",
        "gray-12": "rgb(32, 35, 39)",
        "gray-13": "rgba(231, 233, 234, .1)",

        "gray-15": "#dee0e6",

        /* Black */
        "black-0": "#000000",
        "black-1": "#0f1419",
        "black-2": "#16181c",
        "black-3": "#1d1f23",
        "black-4": "#181818",
        "black-5": "#2f3336",
        "black-6": "rgba(255, 255, 255, .03)",

        /* Blue */
        "blue-0": "rgb(29, 155, 240, .3)",
        "blue-1": "rgb(29, 155, 240, .1)",
        "blue-2": "#e8f5fd",
        "blue-3": "#031018",
        "blue-4": "rgb(36, 45, 52)",
        "blue-5": "#1a8cd8",
        "blue-6": "#1da1f2",

        /* Violet */
        "violet-0": "#794bc4",

        /* Green */
        "green-0": "rgb(0, 186, 124)",
        "green-1": "rgb(0, 186, 124, .3)",
        "green-2": "rgb(0, 186, 124, .1)",

        /* Red */
        "red-0": "rgb(249, 24, 128)",
        "red-1": "rgb(249, 24, 128, .3)",
        "red-2": "rgb(249, 24, 128, .1)",
        "red-3": "rgb(239, 68, 68)",

        /* Orange */
        "orange-0": "#fb932c",
      },
    },
  },
  plugins: [],
}

