HzGov_NotificationStationInfo = {}

function HzGov_NotificationStationInfo.notification_stationInfo(jsonStr)
    local jsonData, err = json.decode(jsonStr)
    if err then 
        return nil, err 
    end
    local convertedData = {
        StationInfo = nil
    }

    local stationInfo = {
        StationID = jsonData.stationId,
        OperatorID = jsonData.operatorId,
        EquipmentOwnerID = jsonData.equipmentOwnerId,
        StationName = jsonData.stationName,
        CountryCode = jsonData.countryCode,
        AreaCode = jsonData.areaCode,
        Address = jsonData.address,
        StationTel = jsonData.stationTel,
        ServiceTel = jsonData.serviceTel,
        StationType = jsonData.stationType,
        StationStatus = jsonData.stationStatus,
        ParkNums = jsonData.parkNums,
        StationLng = jsonData.stationLng,
        StationLat = jsonData.stationLat,
        SiteGuide = jsonData.siteGuide,
        Construction = jsonData.construction,
        MatchCars = jsonData.matchCars,
        ParkInfo = jsonData.parkInfo,
        BusineHours = jsonData.busineHours,
        ElectricityFee = jsonData.electricityFee,
        ServiceFee = jsonData.serviceFee,
        ParkFee = jsonData.parkFee,
        Payment = jsonData.payment,
        SupportOrder = jsonData.supportOrder,
        EquipmentInfos = {}
    }
    for _, equipment in ipairs(jsonData.equipmentInfos) do
        local equipmentInfo = {
            EquipmentID = equipment.equipmentTpid,
            ManufacturerID = equipment.manufacturerId,
            ManufacturerName = equipment.manufacturerName,
            EquipmentModel = equipment.equipmentModel,
            ProductionDate = equipment.productionDate,
            EquipmentType = equipment.equipmentType,
            EquipmentLng = equipment.equipmentLng,
            EquipmentLat = equipment.equipmentLat,
            Power = equipment.power,
            EquipmentName = equipment.equipmentName,
            ConnectorInfos = {}
        }

        for _, connector in ipairs(equipment.connectorInfos) do
            local connectorInfo = {
                ConnectorID = connector.connectorTpid,
                ConnectorName = connector.connectorName,
                ConnectorType = connector.connectorType,
                VoltageUpperLimits = connector.voltageUpperLimits,
                VoltageLowerLimits = connector.voltageLowerLimits,
                Current = connector.current,
                Power = connector.power,
                ParkNo = connector.parkNo,
                NationalStandard = connector.nationalStandard,
            }
            table.insert(equipmentInfo.ConnectorInfos, connectorInfo)
        end
        table.insert(stationInfo.EquipmentInfos, equipmentInfo)
    end
    convertedData.StationInfo = stationInfo

    return json.encode(convertedData)

end
