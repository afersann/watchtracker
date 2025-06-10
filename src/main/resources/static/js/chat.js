document.addEventListener("DOMContentLoaded", function () {
  const chatPopup = document.getElementById("chat-popup");
  const btnMostrar = document.getElementById("btn-mostrar-chat");
  const btnMinimizar = document.getElementById("btn-minimizar-chat");

if (btnMostrar) {
  btnMostrar.addEventListener("click", function () {
    chatPopup.classList.remove("chat-hidden");
  });
}


  if (btnMinimizar) {
    btnMinimizar.addEventListener("click", function () {
      chatPopup.classList.add("chat-hidden");
      btnMostrar.classList.remove("chat-hidden");
    });
  }

  const form = document.getElementById("chat-form");
  if (!form) return;

  const input = document.getElementById("chat-input");
  const chat = document.getElementById("chat-messages");
  const btnEnviar = form.querySelector("button");
  const nombreUsuario = document.getElementById("datos-usuario")?.dataset?.usuario;

  form.addEventListener("submit", function (event) {
    event.preventDefault();
    const mensaje = input.value.trim();
    if (!mensaje) return;

    chat.innerHTML += `<div><strong>Tú:</strong> ${mensaje}</div>`;

    fetch("/api/chat", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        'username': nombreUsuario || 'anonimo'
      },
      body: JSON.stringify({ mensaje })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error(`Error HTTP: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      const respuesta = data.respuesta || "⚠️ Respuesta vacía.";
      chat.innerHTML += `<div><strong>IA:</strong> ${respuesta}</div>`;
      input.value = "";

      // 🔽 Desplaza hacia el último mensaje
      chat.scrollTop = chat.scrollHeight;

      // Si se ha alcanzado el límite diario
      if (respuesta.includes("⏳ Has alcanzado el límite")) {
        input.disabled = true;
        input.classList.add("chat-desactivado");
        btnEnviar.disabled = true;
        btnEnviar.classList.remove("btn-primary");
        btnEnviar.classList.add("btn-secondary");
      }

      // Si la IA está desactivada globalmente
      if (respuesta.includes("🔒 La IA está desactivada")) {
        input.disabled = true;
        input.classList.add("chat-desactivado");
        btnEnviar.disabled = true;
        btnEnviar.classList.remove("btn-primary");
        btnEnviar.classList.add("btn-secondary");
      }
    })
    .catch(error => {
      console.error("Fallo al contactar con la IA:", error);
      chat.innerHTML += `<div><strong>IA:</strong> ⚠️ Error al contactar con la IA (${error.message}).</div>`;
    });
  });
});
