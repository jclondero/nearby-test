import React, { useEffect, useMemo } from 'react'
import {
  NativeEventEmitter,
  NativeModules,
  PermissionsAndroid,
  Text,
  View,
} from 'react-native'
import {
  addOnErrorListener,
  checkBluetoothAvailability,
  checkBluetoothPermission,
  connect,
  publish,
  subscribe,
  useNearbySubscription,
} from './NearbyLib'

const { GoogleNearbyMessages } = NativeModules
const nearbyEventEmitter = new NativeEventEmitter(GoogleNearbyMessages)

const apiKey = 'AIzaSyA9OWEVaTV-MbLZJFgFGkk-axi4M98BQ_I'

function removeAllListeners(event: any): void {
  nearbyEventEmitter.removeAllListeners(event)
}

const App = () => {
  // console.log({ GoogleNearbyMessages })
  const nearbyConfig = useMemo(() => ({ apiKey }), [])
  const { nearbyMessages, nearbyStatus } = useNearbySubscription(nearbyConfig)

  console.log({ nearbyMessages, nearbyStatus })

  useEffect(() => {
    checkBluetoothPermission().then((hasPermission) => {
      console.log({ hasPermission })
      checkBluetoothAvailability().then((isBluetoothAvailable) => {
        console.log({ isBluetoothAvailable })
        if (hasPermission && isBluetoothAvailable) {
          connect({ apiKey }).then(async () => {
            const removeListener = addOnErrorListener((kind, message) =>
              console.error(`${kind}: ${message}`)
            )
            subscribe(
              (m) => {
                console.log(`new message found: ${m}`)
              },
              (m) => {
                console.log(`message lost: ${m}`)
              }
            )
            await publish('hello !').then(() => console.log('publicou'))
          })
        }
      })
    })

    return () => {
      console.log('limpou')
      GoogleNearbyMessages.unpublish()
      GoogleNearbyMessages.unsubscribe()
      GoogleNearbyMessages.disconnect()
      removeAllListeners('MESSAGE_FOUND')
      removeAllListeners('MESSAGE_LOST')
    }
  }, [])

  return (
    <View>
      <Text>My app</Text>
    </View>
  )
}

export default App
