
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const THERMOSTAT = "1";
var serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
      credential: admin.credential.cert(serviceAccount)
});

const receiveAssistantEvent = functions.https.onRequest((req, res) => {
      let authToken = req.headers.authorization ? req.headers.authorization.split(' ')[1] : null;
      let intent = req.body.inputs[0].intent;

      let reqBody = req.body;
      if (!reqBody.inputs) { return; }

      switch (intent) {
            case "action.devices.SYNC":
                  sync(reqBody, res);
                  return;
            case "action.devices.QUERY":
                  query(reqBody, res);
                  return;
            case "action.devices.EXECUTE":
                  execute(reqBody, res);
                  return;
      }

});

function sync(reqBody, res) {
      let deviceProperties = {
            requestId: reqBody.requestId,
            payload: {
                  devices: [
                        {
                              id: THERMOSTAT,
                              type: "action.devices.types.THERMOSTAT",
                              traits: [
                                    "action.devices.traits.TemperatureSetting"
                              ],
                              attributes: {
                                    availableThermostatModes: "on,off,cool,heat,heatcool",
                                    thermostatTemperatureUnit: "C"
                              },
                              name: {
                                    name: "thermostat"
                              },
                              willReportState: false
                        }
                  ]
            }
      };
      res.status(200).json(deviceProperties);
}

function query(reqBody, res) {
      queryFromFirestore('thermostat', thermostat => {

            var tempSetPoint = thermostat.setpoint;
            var tempValue = thermostat.temp;
            var thermostatMode = thermostat.mode;

            let deviceStates = {
                  requestId: reqBody.requestId,
                  payload: {
                        devices: {
                              "1": {
                                    online: true,
                                    thermostatMode: thermostatMode,
                                    thermostatTemperatureSetpoint: tempSetPoint,
                                    thermostatTemperatureAmbient: tempValue
                              }
                        }
                  }
            };

            res.status(200).json(deviceStates);

      })
}

function execute(reqBody, res) {
      queryFromFirestore('thermostat', thermostat => {

            let reqCommands = reqBody.inputs[0].payload.commands;

            let sendCommands = [];

            for (let i = 0; i < reqCommands.length; i++) {
                  let curCommand = reqCommands[i];
                  for (let j = 0; j < curCommand.execution.length; j++) {
                        let curExec = curCommand.execution[j];

                        if (curExec.command === "action.devices.commands.ThermostatTemperatureSetpoint") {
                              for (let k = 0; k < curCommand.devices.length; k++) {
                                    let curDevice = curCommand.devices[k];
                                    if (curDevice.id === THERMOSTAT) {
                                          let thermostatTemperatureSetpoint = curExec.params.thermostatTemperatureSetpoint;
                                          thermostat.setpoint = thermostatTemperatureSetpoint;
                                    }
                                    sendCommands.push({ ids: [curDevice.id], status: "SUCCESS" });
                              }
                        } else if (curExec.command === "action.devices.commands.ThermostatSetMode") {
                              for (let k = 0; k < curCommand.devices.length; k++) {
                                    let curDevice = curCommand.devices[k];
                                    if (curDevice.id === THERMOSTAT) {
                                          let thermostatMode = curExec.params.thermostatMode;
                                          thermostat.mode = thermostatMode;
                                    }
                                    sendCommands.push({ ids: [curDevice.id], status: "SUCCESS" });
                              }
                        }
                  }
            }

            let resBody = {
                  requestId: reqBody.requestId,
                  payload: { commands: sendCommands }
            };

            console.log(resBody);

            updateFirestore('thermostat', thermostat);

            res.status(200).json(resBody);
      });
}

function updateFirestore(ref, doc) {
      admin.firestore()
            .collection('devices')
            .doc(ref)
            .update(doc);
}

function queryFromFirestore(ref, action) {
      admin.firestore()
            .collection('devices')
            .doc(ref)
            .get()
            .then(doc => { action(doc.data()); })
            .catch(err => { console.log('Error getting documents', err); });
}

module.exports = {
      receiveAssistantEvent
};