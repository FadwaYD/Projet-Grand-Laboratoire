import React, { useState } from "react";
import axios from "axios";

function App() {
  const [nom, setNom] = useState("");

  const envoyer = () => {
    axios.post("/backend-j2ee/api/ajouter", {
      nom: nom
    })
    .then(res => {
      console.log(res.data);
      alert(res.data.message);
    })
    .catch(err => {
      console.error(err);
    });
  };

  return (
    <div>
      <h2>Test connexion React → J2EE</h2>
      <input onChange={(e) => setNom(e.target.value)} />
      <button onClick={envoyer}>Envoyer</button>
    </div>
  );
}

export default App;