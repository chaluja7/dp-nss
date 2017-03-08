export class AppSettings {

    public static API_ENDPOINT = '/api/v1/';

    public static SCHEMA_URL_PREFIX = 'x-';

    public static AUTH_HEADER = 'X-Auth';

    public static getSchemaUrlParam(schema: string) {
        return AppSettings.SCHEMA_URL_PREFIX + schema;
    }

}