exports.capitalize = (inputString) => {
  if (inputString === '') {
    throw Error('ERROR: la cadena de entrada está vacía');
  }

  inputString = inputString.toLocaleLowerCase();

  const nombre = inputString.split(' ');

  nombre[0] = nombre[0][0].toUpperCase() + nombre[0].substr(1);

  return nombre[0];
};
