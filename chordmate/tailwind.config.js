/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/main/resources/templates/**/*.html',  // ให้ Tailwind scan Thymeleaf templates
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
