const deleteBtn = document.getElementById("delete-banner");
const banner = document.getElementById("banner");
const uploadBannerBtn = document.getElementById("upload-banner");
const uploadProfilePhoto = document.getElementById("upload-photo");

deleteBtn.addEventListener("click", (e) => {
  e.preventDefault();
  banner.classList.toggle("opacity-0");
});

// No sé cómo seleccionar una imagen y directamente mostrarla sin haberla guardado en ningún lado
